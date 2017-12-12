package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.model.BindEquipmentModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.LongToothRspModel;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 *
 * Created by shc on 2017/12/6.
 */

public class HorizontalRecyclerFragmentHelper {
    public static final String TAG = HorizontalRecyclerFragmentHelper.class.getName();
    static Gson gson = new Gson();
    public static void update(final Context context, final EquipmentRspModel.ListBean plantModel){



//
//        BindEquipmentModel model = new BindEquipmentModel("update", plantModel.getPSIGN());
//        String request = gson.toJson(model);
//        //请求接口进行升级
//        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
//                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
//                    @Override
//                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
//                                                      String service_str, int data_type, byte[] args,
//                                                      LongToothAttachment attachment) {
//                        String result = new String(args);
//                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
//                        int code = longToothRspModel.getCODE();
//                        if (code == 0){
////                            Application.showToast("升级成功");
//                            BindEquipmentModel model = new BindEquipmentModel("isUpdate", plantModel.getPSIGN());
//                            String request = gson.toJson(model);
//                            //请求接口，判断是否需要更新
//                            LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
//                                    0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
//                                        @Override
//                                        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
//                                                                          String service_str, int data_type, byte[] args,
//                                                                          LongToothAttachment attachment) {
//                                            String result = new String(args);
//                                            LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
//                                            int code = longToothRspModel.getCODE();
//                                            switch (code) {
//                                                case 501:
//                                                    //  501:有升级的新版本
//                                                    Log.d(TAG, "handleServiceResponse:升级失败 ");
//                                                    break;
//                                                default:
//                                                    Log.d(TAG, "handleServiceResponse:升级成功 ");
//                                                    Application.showToast("升级成功");
//                                                    break;
//                                            }
//                                        }
//                                    });
//
//
//
//
//
//
//
//
//
//
//
//                        }else {
//                            Application.showToast("升级失败，稍后再试");
//                        }
//                    }
//                });






        BindEquipmentModel model = new BindEquipmentModel("isUpdate", plantModel.getPSIGN());
        String request = gson.toJson(model);
        //请求接口，判断是否需要更新
        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {
                        String result = new String(args);
                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                        int code = longToothRspModel.getCODE();
                        switch (code) {
                            case 501:
                                //  501:有升级的新版本
                                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                                deleteDialog.setTitle("有新版本，确定升级？");
                                deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
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
                                                        if (code == 0){
                                                            BindEquipmentModel model = new BindEquipmentModel("isUpdate", plantModel.getPSIGN());
                                                            String request = gson.toJson(model);
                                                            //请求接口，判断是否需要更新
                                                            LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                                                                    0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                                                                        @Override
                                                                        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                                                                          String service_str, int data_type, byte[] args,
                                                                                                          LongToothAttachment attachment) {
                                                                            String result = new String(args);
                                                                            LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                                                                            int code = longToothRspModel.getCODE();
                                                                            switch (code) {
                                                                                case 501:
                                                                                    //  501:有升级的新版本
                                                                                    Log.d(TAG, "handleServiceResponse:升级失败 ");
                                                                                    break;
                                                                                default:
                                                                                    Application.showToast("升级成功");
                                                                                    break;
                                                                            }
                                                                        }
                                                                    });
                                                        }else {
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






}
