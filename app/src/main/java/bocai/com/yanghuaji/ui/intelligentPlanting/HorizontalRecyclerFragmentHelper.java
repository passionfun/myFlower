package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.model.BindEquipmentModel;
import bocai.com.yanghuaji.model.EquipmentDataModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.LedSetRspModel;
import bocai.com.yanghuaji.model.LongToothRspModel;
import bocai.com.yanghuaji.model.PushModel;
import bocai.com.yanghuaji.util.UiTool;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * Created by shc on 2017/12/6.
 */

public class HorizontalRecyclerFragmentHelper {
    private static final String TAG = HorizontalRecyclerFragmentHelper.class.getName();
    private static Gson gson = new Gson();
    private static Activity mActivity;

    public static void update(final Activity activity, final EquipmentRspModel.ListBean plantModel) {
        mActivity = activity;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                UiTool.showLoading(activity);
            }
        });
        BindEquipmentModel model = new BindEquipmentModel("isUpdate", plantModel.getPSIGN());
        String request = gson.toJson(model);
        //请求接口，判断是否需要更新
        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                UiTool.hideLoading();
                            }
                        });
                        if (args == null) {
                            Application.showToast("未知错误");
                            return;
                        }
                        String result = new String(args);
                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                        int code = longToothRspModel.getCODE();
                        switch (code) {
                            case 501:
                                //  501:有升级的新版本
                                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(activity);
                                deleteDialog.setTitle("有新版本，确定升级？");
                                deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Run.onUiAsync(new Action() {
                                            @Override
                                            public void call() {
                                                UiTool.showLoading(activity);
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
                                                        String result = new String(args);
                                                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                                                        int code = longToothRspModel.getCODE();
                                                        if (code == 0) {
                                                            checkUpdateState(plantModel);
                                                        } else {
                                                            Run.onUiAsync(new Action() {
                                                                @Override
                                                                public void call() {
                                                                    UiTool.hideLoading();
                                                                }
                                                            });
                                                            Application.showToast("升级失败，稍后再试");
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

                                break;
                            default:
                                Application.showToast("已是最新版本");
                                break;
                        }
                    }
                });
    }


    private static void checkUpdateState(final EquipmentRspModel.ListBean plantModel) {
        final BindEquipmentModel model = new BindEquipmentModel("checkUpdateStat", plantModel.getPSIGN());
        String request = gson.toJson(model);
        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {
                        if (args == null) {
                            return;
                        }
                        String result = new String(args);
                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                        Log.d(TAG, "update: " + result);
                        int code = longToothRspModel.getUpdateStat();
                        if (code == 1) {//code=1:正在升级
                            // 循环等待
                            mActivity.getWindow().getDecorView()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            checkUpdateState(plantModel);
                                        }
                                    }, 1000);
                        } else if (code == 2) {//code=2:升级成功
                            Run.onUiAsync(new Action() {
                                @Override
                                public void call() {
                                    UiTool.hideLoading();
                                }
                            });
                            Application.showToast("升级成功");
                        } else if (code == 3) {//code=3:升级失败
                            int times = 0;
                            times++;
                            if (times <= 2) {
                                // 循环等待
                                mActivity.getWindow().getDecorView()
                                        .postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkUpdateState(plantModel);
                                            }
                                        }, 1000);
                            } else {
                                Run.onUiAsync(new Action() {
                                    @Override
                                    public void call() {
                                        UiTool.hideLoading();
                                    }
                                });
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
                        if (args == null) {
                            Application.showToast("未知错误");
                            return;
                        }
                        String result = new String(args);
//                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
//                        int code = longToothRspModel.getCODE();
                    }
                });
    }


    private static boolean isHaveNew = false;

    public static boolean isHaveNewVersion(final EquipmentRspModel.ListBean plantModel) {
        /**
         * 是否有新版本请求格式
         * {
         * "CMD": "isUpdate",
         * "UUID": 1504608600
         * }
         */
        if (TextUtils.isEmpty(plantModel.getLTID()) || TextUtils.isEmpty(plantModel.getPSIGN())) {
            return false;
        }
        BindEquipmentModel model = new BindEquipmentModel("isUpdate", plantModel.getPSIGN());
        String request = gson.toJson(model);
        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new MyLongToothServiceResponseHandler());

        return isHaveNew;
    }

    static class MyLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        @Override
        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                          String service_str, int data_type, byte[] args,
                                          LongToothAttachment attachment) {
            String result = new String(args);
            LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
            Log.d(TAG, "update:" + result);
            int code = longToothRspModel.getCODE();
            switch (code) {
                case 501:
                    //  501:有升级的新版本
                    isHaveNew = true;
                    break;
                default:
                    isHaveNew = false;
                    break;
            }
        }
    }


    private static boolean isLedOn = false;

    public static boolean getLedStatus(EquipmentRspModel.ListBean plantModel) {
        BindEquipmentModel model = new BindEquipmentModel("LedSet", plantModel.getPSIGN());
        String request = gson.toJson(model);
        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new MyLedStatusLongToothServiceResponseHandler());
        return isLedOn;
    }


    static class MyLedStatusLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        @Override
        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                          String service_str, int data_type, byte[] args,
                                          LongToothAttachment attachment) {
            String result = new String(args);
            LedSetRspModel ledSetRspModel = gson.fromJson(result, LedSetRspModel.class);
            Log.d(TAG, "ledSetRspModel111:" + result);
            int code = ledSetRspModel.getCODE();
            if (code==0){
                String ledStatus = ledSetRspModel.getSWTICH();
                if (ledStatus!=null&&ledStatus.equals("On")){
                    isLedOn = true;
                }else {
                    isLedOn = false;
                }

            }
        }
    }

}
