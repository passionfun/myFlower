package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Timer;

import bocai.com.yanghuajien.model.LightOnModel;
import bocai.com.yanghuajien.model.LongToothRspModel;
import bocai.com.yanghuajien.model.NoDisturbModel;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * 作者 yuanfei on 2017/12/4.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentSettingHelper {
    public static final String TAG = EquipmentSettingHelper.class.getName();
    static Gson gson = new Gson();
    /**
     * "CMD": "lightOn",
     * "Begin": 73800,
     * "UUID": 1504608600
     */
    public static void setLightOn(String begin,String uuid,String longToothId) {

        LightOnModel model = new LightOnModel();
        model.setCMD("lightOn");
        model.setBegin(begin);
        model.setUUID(uuid);


        String request = gson.toJson(model);

//        LongTooth.request(longToothId,"longtooth", LongToothTunnel.LT_ARGUMENTS,request.getBytes(),0,request.getBytes().length,null,new LongToothResponse());
        LongTooth.request(longToothId,"longtooth", LongToothTunnel.LT_ARGUMENTS,request.getBytes(),
                0,request.getBytes().length,null,new LongToothResponse());
    }



    public static void setNoDisturb(String begin,String end,String uuid,String longToothId){
        NoDisturbModel model = new NoDisturbModel();
        model.setBegin(begin);
        model.setEnd(end);
        model.setCMD("noDisturb");
        model.setUUID(uuid);
        final String request = gson.toJson(model);
        LongTooth.request(longToothId, "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(), 0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
            @Override
            public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                              String service_str, int data_type, byte[] args,
                                              LongToothAttachment attachment) {
                if (args==null){
                    return;
                }
                String result = new String(args);
                if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                    return;
                }
                LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                Log.d(TAG, "noDisturb: "+result);
                if (longToothRspModel.getCODE()==0){
                    EquipmentSettingActivity.isSetNoDisturbSuccess = true;
//                    Application.showToast("设置免打扰成功");
                }else {
//                    Application.showToast("设置免打扰失败");
                }
            }
        });
    }


    private static Timer timer = new Timer();


     static class LongToothResponse implements LongToothServiceResponseHandler {

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
            Log.d(TAG, "lightOn: "+result);
            LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
            if (longToothRspModel.getCODE()==0){
                EquipmentSettingActivity.isSetLightOnSuccess = true;
//                Application.showToast("设置补光开启时间成功");
            }else {
//                Application.showToast("设置补光开启时间失败");
            }

        }
    }

}
