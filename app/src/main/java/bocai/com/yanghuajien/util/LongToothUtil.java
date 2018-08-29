package bocai.com.yanghuajien.util;

import android.util.Log;

import com.google.gson.Gson;

import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.common.Factory;
import bocai.com.yanghuajien.ui.intelligentPlanting.SampleAttachment;
import bocai.com.yanghuajien.util.persistence.Account;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothEvent;
import xpod.longtooth.LongToothEventHandler;
import xpod.longtooth.LongToothServiceRequestHandler;
import xpod.longtooth.LongToothTunnel;

/**
 *
 * Created by shc on 2017/12/20.
 */

public class LongToothUtil {
    public static final String TAG = "LongToothUtil";
    public static boolean isInitSuccess = false;

    public static void longToothInit(){
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    //启动长牙
                    LongTooth.setRegisterHost(Account.getRegisterHost(), Integer.valueOf(Account.getPort()));
                    LongTooth.start(Application.getInstance(),
                            Integer.valueOf(Account.getDevelopId()),
                            Integer.valueOf(Account.getAppId()),
                            Account.getAppKey(),
                            new LongToothHandler());
                    LogUtil.d(TAG,"长牙信息longtooth info: "+  Account.getRegisterHost() + "\nport:" + Integer.valueOf(Account.getPort()) + "\nDevelopId:" + Integer.valueOf(Account.getDevelopId()) + "\nAppId:" + Integer.valueOf(Account.getAppId()) + "\nappkey:" + Account.getAppKey());
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.d(TAG,"长牙信息longtooth info(exp):"+e.getMessage());
                }
            }
        });
    }


    private static class LongToothHandler implements LongToothEventHandler {
        @Override
        public void handleEvent(int code, String ltid_str, String srv_str, byte[] msg, LongToothAttachment attachment) {
//            if (code == LongToothEvent.EVENT_LONGTOOTH_STARTED) {
//
//            }
            LogUtil.d(TAG,"长牙初始化回调code："+code);
            if (code == LongToothEvent.EVENT_LONGTOOTH_STARTED) {//长牙服务端与客户端连接被建立131073
            } else if (code == LongToothEvent.EVENT_LONGTOOTH_ACTIVATED) {//长牙成功连接到长牙服务器131074
                isInitSuccess = true;
                LongTooth.addService("n22s", new LongToothNSServer());
                LongTooth.addService(Account.getServiceName(), new LongToothServer());
            } else if (code == LongToothEvent.EVENT_LONGTOOTH_OFFLINE) {//本地长牙不在线163844
                Log.d("shcbind", "handleEvent:1 "+code);
            } else if (code == LongToothEvent.EVENT_LONGTOOTH_TIMEOUT) {//长牙响应超时163842
                Log.d("shcbind", "handleEvent:2 "+code);
            } else if (code == LongToothEvent.EVENT_LONGTOOTH_UNREACHABLE) {//远程长牙不可访问163843
                Log.d("shcbind", "handleEvent:3 "+code);
            } else if (code == LongToothEvent.EVENT_SERVICE_NOT_EXIST) {//调用的远程服务不存在262145
                Log.d("shcbind", "handleEvent:4 "+code);
            }
        }
    }


    /**
     * Handler the n22s request
     * */
    private static class LongToothNSServer implements LongToothServiceRequestHandler {

        @Override
        public void handleServiceRequest(LongToothTunnel arg0, String arg1,
                                         String arg2, int arg3, byte[] arg4) {
            try {

                if (arg4 != null) {
                    byte[] b = "n22s response---".getBytes();
                    SampleAttachment a = new SampleAttachment();
                    LongTooth.respond(arg0, LongToothTunnel.LT_ARGUMENTS, b, 0,
                            b.length, a);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static int  isResponse = 0;
    /**
     * Handler the longtooth service request
     * */
    private static class LongToothServer implements LongToothServiceRequestHandler {

        @Override
        public void handleServiceRequest(LongToothTunnel arg0, String arg1,
                                         String arg2, int arg3, byte[] arg4) {
            try {
                if (arg4 != null) {
                    byte[] b = "longtooth response:".getBytes();
                    SampleAttachment a = new SampleAttachment();
                    LongTooth.respond(arg0, LongToothTunnel.LT_ARGUMENTS, b, 0,
                            b.length, a);
                    if(isResponse<307){
                        Log.d("shcbind", "handleServiceRequest: "+307);
//                        LongTooth.request(serverLongToothId, servername,
//                                LongToothTunnel.LT_ARGUMENTS, sb.toString().getBytes(), 0,
//                                sb.toString().getBytes().length, new SampleAttachment(),
//                                new LongToothResponse());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




}
