package bocai.com.yanghuaji.util;

import android.util.Log;

import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.common.Factory;
import bocai.com.yanghuaji.ui.intelligentPlanting.SampleAttachment;
import bocai.com.yanghuaji.util.persistence.Account;
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

    public static boolean isInitSuccess = false;

    public static void longToothInit(){
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    //启动长牙
                    Log.d("shcbind", "run: "+Account.getRegisterHost()+"\n"+
                    "port:"+Integer.valueOf(Account.getPort())+"\n"
                    +"DevelopId:"+Integer.valueOf(Account.getDevelopId())+"\n"
                            +"AppId:"+Integer.valueOf(Account.getAppId())+"\n"
                    +"appkey:"+Account.getAppKey());
                    LongTooth.setRegisterHost(Account.getRegisterHost(), Integer.valueOf(Account.getPort()));
                    LongTooth.start(Application.getInstance(),
                            Integer.valueOf(Account.getDevelopId()),
                            Integer.valueOf(Account.getAppId()),
                            Account.getAppKey(),
                            new LongToothHandler());

                } catch (Exception e) {
                    e.printStackTrace();
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
            Log.d("shcbind", "handleEvent: "+code);

            if (code == LongToothEvent.EVENT_LONGTOOTH_STARTED) {
            } else if (code == LongToothEvent.EVENT_LONGTOOTH_ACTIVATED) {
                isInitSuccess = true;
                LongTooth.addService("n22s", new LongToothNSServer());
                LongTooth.addService(Account.getServiceName(), new LongToothServer());
            } else if (code == LongToothEvent.EVENT_LONGTOOTH_OFFLINE) {
                Log.d("shcbind", "handleEvent:1 "+code);
            } else if (code == LongToothEvent.EVENT_LONGTOOTH_TIMEOUT) {
                Log.d("shcbind", "handleEvent:2 "+code);
            } else if (code == LongToothEvent.EVENT_LONGTOOTH_UNREACHABLE) {
                Log.d("shcbind", "handleEvent:3 "+code);
            } else if (code == LongToothEvent.EVENT_SERVICE_NOT_EXIST) {
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
