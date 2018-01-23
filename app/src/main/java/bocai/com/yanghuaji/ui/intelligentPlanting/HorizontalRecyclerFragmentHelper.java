package bocai.com.yanghuaji.ui.intelligentPlanting;

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

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.model.BindEquipmentModel;
import bocai.com.yanghuaji.model.EquipmentDataModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.LedSetRspModel;
import bocai.com.yanghuaji.model.LongToothRspModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.model.PushModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.MainRecylerContract;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
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
        deleteDialog.setTitle("有新版本，确定升级？");
        deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        progressDialog = UiTool.showProgressBarDialog(activity);
                        timer.schedule(new TimerTask() {
                            int times = 1;

                            @Override
                            public void run() {
                                times++;
                                if (times > 99) {
                                    this.cancel();
                                    timer.cancel();
                                    Application.showToast("升级失败");
                                    progressDialog.cancel();
                                }
                                progressDialog.setProgress(times);

                            }
                        }, 1500, 1500);
                        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "停止", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.cancel();
                                timer.cancel();
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
                                    Application.showToast("升级失败");
                                    progressDialog.cancel();
                                }
                            }
                        });
            }
        });
        deleteDialog.setNegativeButton("取消", null);
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
                            timer.cancel();
                            progressDialog.cancel();
                            EventBus.getDefault().post(new MessageEvent(UPDATE_SUCCESS));
                            Application.showToast("升级成功");
                        } else if (code == 3) {//code=3:升级失败
                            failTimes++;
                            if (failTimes > 2)  {
                                failTimes=0;
                                checkUpdateStateTimer.cancel();
                                timer.cancel();
                                progressDialog.cancel();
                                Application.showToast("升级失败");
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
        Log.d(TAG, "equipmentReset: ");
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
            Log.d(TAG, "equipmentReset: args");
            if (args==null)
                return;
            String result = new String(args);
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
                                            mLedMode.setText("台灯");
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
                                            mLedMode.setText("补光");
                                        }
                                    });

                                    break;
                                default:
                                    Run.onUiAsync(new Action() {
                                        @Override
                                        public void call() {
                                            mLedMode.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_light,0,0,0);
                                            mLedMode.setText("待机");
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
            if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                return;
            }
            LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
            Log.d(TAG, "update:" + result);
            int code = longToothRspModel.getCODE();
            switch (code) {
                case 501:
                    //  501:有升级的新版本
                    if (isHorizontal) {
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                mUpdate.setText("设备升级");
                                mUpdate.setEnabled(true);
                                mUpdate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_update_horizontal, 0, 0);
                            }
                        });
                    } else {
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                mUpdate.setText("设备升级");
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
                                mUpdate.setText("最新版本");
                                mUpdate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_update_horizontal_nomal, 0, 0);
                            }
                        });
                    } else {
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                mUpdate.setText("最新版本");
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
                return "正常";
            case "2":
                return "偏低";
            case "3":
                return "缺水";
            default:
                return HorizontalRecyclerFragment.UNKNOWN;
        }
    }

}
