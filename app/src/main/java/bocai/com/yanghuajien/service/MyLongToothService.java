package bocai.com.yanghuajien.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.ui.intelligentPlanting.SampleAttachment;
import bocai.com.yanghuajien.util.ConstUtil;
import bocai.com.yanghuajien.util.LogUtil;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothEventHandler;
import xpod.longtooth.LongToothServiceRequestHandler;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * Created by zhanghuanhuan on 2018/4/11.
 */

public class MyLongToothService extends IntentService {
    private static final String TAG = "MyLongToothService";
    public static MyLongToothListener listener;

    public static void setMyLongToothListener(MyLongToothListener myLongToothListener) {
        listener = myLongToothListener;
    }

    public interface MyLongToothListener {
        void updateUI(Message msg);
        void timeOutMsg(Message msg);
    }

    public static void sendDataFrame(Context mContext, String opType, String ltID, String cmd) {
        Intent mIntent = new Intent(mContext, MyLongToothService.class);
        mIntent.setAction(ConstUtil.ACTION_SEND_FRAME_SUCCESS);
        Bundle sendData = new Bundle();
        sendData.putString("opType", opType);
        sendData.putString("ltid", ltID);
        sendData.putString("cmd", cmd);
        mIntent.putExtra("bundle", sendData);
        mContext.startService(mIntent);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MyLongToothService() {
        super("MyLongToothService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtil.d(TAG,"onHandleIntent");
        if (intent != null) {
            LogUtil.d(TAG,"onHandleIntent not null");
            if (ConstUtil.ACTION_SEND_FRAME_SUCCESS.equals(intent.getAction())) {
                LogUtil.d(TAG,"onHandleIntent action equal");
                try {
                    Bundle data = intent.getBundleExtra("bundle");
                    String op = data.getString("opType");
                    String ltid = data.getString("ltid");
                    String cmd = data.getString("cmd");
                    LogUtil.d(TAG,"onhandleintent cmd:" + cmd);
                    byte[] dataFrame = cmd.getBytes();
//                    String returnData = BroadLinkUtils.sendDataFrame(dataFrame,mac,op);
                    if (op.equals(ConstUtil.OP_START)) {
                        LogUtil.d(TAG,"onHandleIntent op_start");
                        LongTooth.setRegisterHost(ConstUtil.HOST_NAME, ConstUtil.PORT);
                        LongTooth.start(Application.getInstance(), ConstUtil.DEVID, ConstUtil.APPID, ConstUtil.APPKEY, new MyLongtoothStartHandler(op));
                    } else{
                        LongTooth.request(ltid,
                                ConstUtil.LONGTOOTH_SERVICENAME,
                                LongToothTunnel.LT_ARGUMENTS, dataFrame,
                                0,
                                dataFrame.length, new SampleAttachment(),
                                new MyLongtoothResposeHandler(op));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.d(TAG,"onHandleIntent exception…:"+e.getMessage());
                }
            }
        }
    }

    private class MyLongtoothResposeHandler implements LongToothServiceResponseHandler {
        private String op;
        public MyLongtoothResposeHandler(String op) {
            this.op = op;
        }

        @Override
        public void handleServiceResponse(LongToothTunnel longToothTunnel, String ltid_str, String service_str, int data_type, byte[] result, LongToothAttachment longToothAttachment) {
            LogUtil.d(TAG,"handleServiceRespose:"+Thread.currentThread().getName());
            if (result != null) {
                String res = new String(result);
                if (!TextUtils.isEmpty(res) && res.contains("CODE")) {
                    LogUtil.d(TAG,"LongToothServiceResponseHandler:" + res);
                    if (op.equals(ConstUtil.OP_BIND)) {
                        LogUtil.d(TAG,"LongToothServiceResponseHandler bind success:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.BIND_DEVICE_SUCCESS;
                            message.obj = res +"=="+ltid_str;//用户可以知道绑定哪些设备（成功或者失败）
                            listener.updateUI(message);
                        }
                    } else if (op.equals(ConstUtil.OP_QUERRY)) {
                        LogUtil.d(TAG,"LongToothServiceResponseHandler querry success:" + res +","+ltid_str);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.QUERRY_DEVICE_SUCCESS;
                            message.obj = res +"=="+ltid_str;//发送给绑定设备的列表界面的设备的状态判断（在线或者离线）
                            listener.updateUI(message);
                        }
                    } else if (op.equals(ConstUtil.OP_BRIGHTNESS)) {
                        LogUtil.d(TAG,"LongToothServiceResponseHandler bright success:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.CONTROL_DEVICE_BRIGHTNESS_SUCCESS;
                            message.obj = res;
                            listener.updateUI(message);
                        }
                    } else if(op.equals(ConstUtil.OP_RESET)){
                        LogUtil.d(TAG,"LongToothServiceResponseHandler reset success:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.RESET_DEVICE_SUCCESS;
                            message.obj = res;
                            listener.updateUI(message);
                        }
                    } else if(op.equals(ConstUtil.OP_INTELLIGENT_CONTROL)){
                        LogUtil.d(TAG,"LongToothServiceResponseHandler intelligent control success:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.INTELLIGENT_CONTROL_SUCCESS;
                            message.obj = res;
                            listener.updateUI(message);
                        }
                    }else if (op.equals(ConstUtil.OP_AUTO)) {
                        LogUtil.d(TAG,"LongToothServiceResponseHandler auto control success:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.SMART_CONTROL_AUTO_SUCCESS;
                            message.obj = res+"=="+ltid_str;
                            listener.updateUI(message);
                        }
                    }else if(op.equals(ConstUtil.OP_NODISTURB)){
                        LogUtil.d(TAG,"LongToothServiceResponseHandler noDisturb success:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.NO_DISTURB_SUCCESS;
                            message.obj = res+"=="+ltid_str;
                            listener.updateUI(message);
                        }
                    }else if(op.equals(ConstUtil.OP_LIGHTON)){
                        LogUtil.d(TAG,"LongToothServiceResponseHandler lighton success:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.LIGHT_ON_SUCCESS;
                            message.obj = res+"=="+ltid_str;
                            listener.updateUI(message);
                        }
                    }else if(op.equals(ConstUtil.OP_PUMP_SET)){//设置水泵
                        LogUtil.d(TAG,"LongToothServiceResponseHandler pumpset success:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.PUMP_SET_SUCCESS;
                            message.obj = res+"=="+ltid_str;
                            listener.updateUI(message);
                        }
                    }else if(op.equals(ConstUtil.OP_ISUPDATE)){
                        LogUtil.d(TAG,"LongToothServiceResponseHandler check device isupdate success:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.CHECK_UPDATE_SUCCESS;
                            message.obj = res+"=="+ltid_str;
                            listener.updateUI(message);
                        }
                    }else if(op.equals(ConstUtil.OP_UPDATE_FIRMWARM)){
                        LogUtil.d(TAG,"LongToothServiceResponseHandler device is updating:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.DEVICE_UPDATING;
                            message.obj = res+"=="+ltid_str;
                            listener.updateUI(message);
                        }
                    }else if(op.equals(ConstUtil.OP_UPDATE_STATE)){
                        LogUtil.d(TAG,"LongToothServiceResponseHandler check device update state:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.DEVICE_UPDATE_STATE;
                            message.obj = res+"=="+ltid_str;
                            listener.updateUI(message);
                        }
                    }else if(op.equals(ConstUtil.OP_GET_DEVICE_WORK_MODE)){
                        LogUtil.d(TAG,"LongToothServiceResponseHandler device getOpMode:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.GET_DEVICE_OP_MODE_SUCCESS;
                            message.obj = res+"=="+ltid_str;
                            listener.updateUI(message);
                        }
                    }else if(op.equals(ConstUtil.OP_DEVICE_FIRMWARE_VERSION)){
                        LogUtil.d(TAG,"LongToothServiceResponseHandler device firmware version:" + res);
                        if (listener != null) {
                            Message message = new Message();
                            message.what = ConstUtil.GET_DEVICE_FIRMWARE_VERSION_VERSION;
                            message.obj = res+"=="+ltid_str;
                            listener.updateUI(message);
                        }
                    }

                } else {
                    LogUtil.d(TAG,"LongToothServiceResponseHandler失败");
                }
            }
        }
    }

    private class MyLongtoothStartHandler implements LongToothEventHandler {
        private String op;

        private MyLongtoothStartHandler(String op) {
            this.op = op;
        }

        @Override
        public void handleEvent(int code, String s, String s1, byte[] result, LongToothAttachment longToothAttachment) {
            LogUtil.d(TAG,"handleEvent: " + code);
            if (code != 131073) {
                if (code == 131074) {
                    LogUtil.d(TAG,"131074 begin add service：" + code);
                    String res = new String(result);
                    LongTooth.addService(ConstUtil.LONGTOOTH_SERVICENAME, new MyLongtoothService(op));
                    if (listener != null) {
                        Message message = new Message();
                        message.what = ConstUtil.START_LONGTOOTH_SUCCESS;
                        message.obj = res;
                        listener.updateUI(message);
                    }
                    return;
                }

                if (code == 163844) {//本地长牙不在线
                    LogUtil.d(TAG,"本地长牙不在线: " + code);
                    return;
                }

                if (code == 163842) {//长牙响应超时30-31s
                    LogUtil.d(TAG,"长牙响应超时:" + code);
                    return;
                }

                if (code == 163843) {//远程长牙不可访问
                    LogUtil.d(TAG,"远程长牙不可访问" + code);
                    return;
                }

                if (code == 262145) {//调用的远程服务不存在
                    LogUtil.d(TAG,"调用的远程服务不存在：" + code);
                    return;
                }
            }else{
                LogUtil.d(TAG,"131073=begin add service(zhushidiao)===>：" + code);
//                String res = new String(result);
//                LongTooth.addService(ConstUtil.LONGTOOTH_SERVICENAME, new MyLongtoothService(op));
//                if (listener != null) {
//                    Message message = new Message();
//                    message.what = ConstUtil.START_LONGTOOTH_SUCCESS;
//                    message.obj = res;
//                    listener.updateUI(message);
//                }
            }
        }
    }

    private class MyLongtoothService implements LongToothServiceRequestHandler {
        private String op = "";

        private MyLongtoothService(String op) {
            this.op = op;
        }

        @Override
        public void handleServiceRequest(LongToothTunnel ltt, String s, String s1, int i, byte[] result) {
            if (result != null) {
                try {
                    if (listener != null) {
                        Message message = new Message();
                        message.what = ConstUtil.ADDSERVICE_SUCCESS;
                        message.obj = result.toString();
                        listener.updateUI(message);
                    }
                    LogUtil.d(TAG,"longtooth response:" + new String(result));
                    byte[] data = "longtooth response:".getBytes();
                    SampleAttachment var8 = new SampleAttachment();
                    LongTooth.respond(ltt, 0, data, 0, data.length, var8);
                } catch (Exception var9) {
                    var9.printStackTrace();
                    return;
                }
            }
        }
    }

}
