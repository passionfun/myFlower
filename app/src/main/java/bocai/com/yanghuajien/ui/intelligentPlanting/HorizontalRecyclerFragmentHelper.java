package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Activity;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.model.BindEquipmentModel;
import bocai.com.yanghuajien.model.EquipmentDataModel;
import bocai.com.yanghuajien.model.EquipmentRspModel;
import bocai.com.yanghuajien.model.LedSetRspModel;
import bocai.com.yanghuajien.model.LongToothRspModel;
import bocai.com.yanghuajien.model.MessageEvent;
import bocai.com.yanghuajien.model.PushModel;
import bocai.com.yanghuajien.presenter.intelligentPlanting.MainRecylerContract;
import bocai.com.yanghuajien.util.LogUtil;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * Created by shc on 2017/12/6.
 */

class HorizontalRecyclerFragmentHelper {
    private static final String TAG = HorizontalRecyclerFragmentHelper.class.getName();
    public static final String UPDATE_SUCCESS = "UPDATE_SUCCESS";
    private static Gson gson = new Gson();
    private static Activity mActivity;
    static final String LED_ON = "ledOn";
    static final String LED_OFF = "ledOff";
    static ProgressDialog progressDialog;
    static Timer timer = new Timer();
    static Timer checkUpdateStateTimer = new Timer();

    static void update(final Activity activity, final EquipmentRspModel.ListBean plantModel) {
        mActivity = activity;
        //  501:有升级的新版本
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(activity);
        deleteDialog.setTitle(Application.getInstance().getString(R.string.select));
        deleteDialog.setPositiveButton(Application.getInstance().getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        progressDialog = UiTool.showProgressBarDialog(activity);
                        if (timer ==null)
                            timer=new Timer();
                        timer.schedule(new TimerTask() {
                            int times = 1;
                            @Override
                            public void run() {
                                times++;
                                if (times > 99) {
                                    this.cancel();
                                    timer.cancel();
                                    timer=null;
                                    Application.showToast(Application.getInstance().getString(R.string.update_failed));
                                    progressDialog.cancel();
                                }
                                progressDialog.setProgress(times);

                            }
                        }, 1500, 1500);
                        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                                Application.getInstance().getString(R.string.stop), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.cancel();
                                timer.cancel();
                                timer=null;
                            }
                        });
                    }
                });

                //升级操作
                BindEquipmentModel model = new BindEquipmentModel("update", plantModel.getPSIGN());
                String request = gson.toJson(model);
                //请求接口进行升级
                LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                        0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                            @Override
                            public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                              String service_str, int data_type, byte[] args,
                                                              LongToothAttachment attachment) {
                                if (args==null)
                                    return;
                                String result = new String(args);
                                if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                                    return;
                                }
                                LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                                int code = longToothRspModel.getCODE();
                                if (code == 0) {
                                    checkUpdateStateTimer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            checkUpdateState(plantModel);
                                        }
                                    }, 5000, 5000);
                                } else {
                                    timer.cancel();
                                    timer=null;
                                    Application.showToast(Application.getStringText(R.string.update_failed));
                                    progressDialog.cancel();
                                }
                            }
                        });
            }
        });
        deleteDialog.setNegativeButton(Application.getInstance().getString(R.string.cancel), null);
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                deleteDialog.show();
            }
        });
    }

    static int failTimes = 0;
    private static void checkUpdateState(final EquipmentRspModel.ListBean plantModel) {
        final BindEquipmentModel model = new BindEquipmentModel("checkUpdateStat", plantModel.getPSIGN());
        String request = gson.toJson(model);
        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {
                        if (args==null)
                            return;
                        String result = new String(args);
                        if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                            return;
                        }
                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                        Log.d(TAG, "update: " + result);
                        int code = longToothRspModel.getUpdateStat();
                        if (code == 1) {//code=1:正在升级

                        } else if (code == 2) {//code=2:升级成功
                            checkUpdateStateTimer.cancel();
                            if (timer!=null){
                                timer.cancel();
                                timer=null;
                            }
                            progressDialog.cancel();
                            EventBus.getDefault().post(new MessageEvent(UPDATE_SUCCESS));
                            Application.showToast(Application.getInstance().getString(R.string.update_success));
                        } else if (code == 3) {//code=3:升级失败
                            failTimes++;
                            if (failTimes > 2)  {
                                failTimes=0;
                                checkUpdateStateTimer.cancel();
                                timer.cancel();
                                timer=null;
                                progressDialog.cancel();
                                Application.showToast(Application.getInstance().getString(R.string.update_failed));
                            }
                        }
                    }
                });
    }

    public static void push(EquipmentDataModel modell, PushModel pushModel) {
        String request = gson.toJson(pushModel);
        //请求接口，判断是否需要更新
        LongTooth.request(modell.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {

                        if (args==null)
                            return;
                        String result = new String(args);
                        if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                            return;
                        }
//                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
//                        int code = longToothRspModel.getCODE();
                    }
                });
    }

    public static void equipmentReset(EquipmentRspModel.ListBean modell) {
        BindEquipmentModel resetModel = new BindEquipmentModel("FactoryReset", modell.getPSIGN());
        String request = gson.toJson(resetModel);
        LogUtil.d(TAG, "设备发送出厂设置的数据帧equipmentReset: "+request);
        LongTooth.request(modell.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new EquipmentResetLongToothServiceResponseHandler(modell));

    }

    private static int times = 0;

    static class EquipmentResetLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        private boolean isRsp = false;

        public EquipmentResetLongToothServiceResponseHandler(final EquipmentRspModel.ListBean modell) {
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isRsp) {
                        times++;
                        if (times > 1) {
                            times = 0;
                            return;
                        }
                        equipmentReset(modell);
                    }
                }
            }, 3000);

        }

        @Override
        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                          String service_str, int data_type, byte[] args,
                                          LongToothAttachment attachment) {
            if (args==null)
                return;
            String result = new String(args);
            LogUtil.d(TAG,"恢复出厂设置返回的数据帧："+result);
            if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                return;
            }
            LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
            int code = longToothRspModel.getCODE();
            if (code == 0) {
                isRsp = true;
            }
        }

    }

    public static void setLedMode(final EquipmentRspModel.ListBean modell, final TextView mLedMode, final MainRecylerContract.Presenter mPresenter) {
        BindEquipmentModel resetModel = new BindEquipmentModel("getOpMode", modell.getPSIGN());
        final String request = gson.toJson(resetModel);
        LongTooth.request(modell.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {
                        if (args==null)
                            return;
                        String result = new String(args);
                        if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                            return;
                        }

                        Log.d(TAG, "getOpMode: " + result);
                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                        final int code = longToothRspModel.getCODE();
                        if (code == 0) {
                            final String mode = longToothRspModel.getOpMode();
                            if (mode == null) {
                                return;
                            }
                            if (mPresenter != null) {
                                Run.onUiAsync(new Action() {
                                    @Override
                                    public void call() {
                                        mPresenter.setCheckBox(Account.getToken(), "3", mode + "", modell.getId());
                                    }
                                });
                            }
                            switch (mode) {
                                case "3":
                                    Run.onUiAsync(new Action() {
                                        @Override
                                        public void call() {
                                            mLedMode.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_light_on,0,0,0);
                                            mLedMode.setText("Lamp");
                                        }
                                    });

                                    break;
                                case "0":
                                case "1":
                                case "2":
                                    Run.onUiAsync(new Action() {
                                        @Override
                                        public void call() {
                                            mLedMode.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_light,0,0,0);
                                            mLedMode.setText("Light");
                                        }
                                    });

                                    break;
                                default:
                                    Run.onUiAsync(new Action() {
                                        @Override
                                        public void call() {
                                            mLedMode.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_light,0,0,0);
                                            mLedMode.setText("Standby");
                                        }
                                    });

                                    break;
                            }
                        }
                    }
                });
    }


//    private static boolean isHaveNew = false;

    public static void isHaveNewVersion(final EquipmentRspModel.ListBean plantModel, TextView mUpdate, boolean isHorizontal) {
        /**
         * 是否有新版本请求格式
         * {
         * "CMD": "isUpdate",
         * "UUID": 1504608600
         * }
         */
        if (TextUtils.isEmpty(plantModel.getLTID()) || TextUtils.isEmpty(plantModel.getPSIGN())) {
            return;
        }
        BindEquipmentModel model = new BindEquipmentModel("isUpdate", plantModel.getPSIGN());
        String request = gson.toJson(model);
        LogUtil.d(TAG,"发送检查设备固件是否有新版本的数据帧（isUpdate）："+request);
        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new MyLongToothServiceResponseHandler(mUpdate, isHorizontal));

//        return isHaveNew;
    }

    static class MyLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        private TextView mUpdate;
        private boolean isHorizontal;

        public MyLongToothServiceResponseHandler(TextView mUpdate, boolean isHorizontal) {
            this.mUpdate = mUpdate;
            this.isHorizontal = isHorizontal;
        }

        @Override
        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                          String service_str, int data_type, byte[] args,
                                          LongToothAttachment attachment) {
            if (args==null)
                return;
            String result = new String(args);
            LogUtil.d(TAG,"检查固件升级数据帧（isUpdate）返回："+result);
            if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                return;
            }
            LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
            int code = longToothRspModel.getCODE();
            switch (code) {
                case 501:
                    //  501:有升级的新版本
                    if (isHorizontal) {
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                mUpdate.setText("Device Upgrade");
                                mUpdate.setEnabled(true);
                                mUpdate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_update_horizontal, 0, 0);
                            }
                        });
                    } else {
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                mUpdate.setText("Device Upgrade");
                                mUpdate.setEnabled(true);
                                mUpdate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_update_vertical, 0, 0);
                            }
                        });
                    }

//                    isHaveNew = true;
                    break;
                default:
                    if (isHorizontal) {
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                mUpdate.setEnabled(false);
                                mUpdate.setText("Latest Version");
                                mUpdate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_update_horizontal_nomal, 0, 0);
                            }
                        });
                    } else {
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                mUpdate.setText("Latest Version");
                                mUpdate.setEnabled(false);
                                mUpdate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_update_vertical_nomal, 0, 0);
                            }
                        });
                    }
//                    isHaveNew = false;
                    break;
            }
        }
    }


    public static void setLedSwitch(EquipmentRspModel.ListBean plantModel) {
        BindEquipmentModel model = new BindEquipmentModel("LedSet", plantModel.getPSIGN());
        String request = gson.toJson(model);
        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new MyLedStatusLongToothServiceResponseHandler(plantModel));
    }

    static class MyLedStatusLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        EquipmentRspModel.ListBean plantModel;

        public MyLedStatusLongToothServiceResponseHandler(EquipmentRspModel.ListBean plantModel) {
            this.plantModel = plantModel;
        }

        @Override
        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                          String service_str, int data_type, byte[] args,
                                          LongToothAttachment attachment) {
            if (args==null)
                return;
            String result = new String(args);
            if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                return;
            }
            LedSetRspModel ledSetRspModel = gson.fromJson(result, LedSetRspModel.class);
            Log.d(TAG, "ledSetRspModel111:" + result);
            int code = ledSetRspModel.getCODE();
            if (code == 0) {
                String ledStatus = ledSetRspModel.getSWITCH();
                if (ledStatus != null && ledStatus.equals("On")) {
                    EventBus.getDefault().post(new MessageEvent(LED_ON,plantModel.getLTID()));
                } else {
                    EventBus.getDefault().post(new MessageEvent(LED_OFF,plantModel.getLTID()));
                }

            }
        }
    }


    public static String getWaStatus(String code) {
        switch (code) {
            case "1":
                return Application.getStringText(R.string.normal);
            case "2":
                return Application.getStringText(R.string.too_low);
            case "3":
                //缺水
                return Application.getStringText(R.string.water_Shortage);
            default:
                return HorizontalRecyclerFragment.UNKNOWN;
        }
    }

}
