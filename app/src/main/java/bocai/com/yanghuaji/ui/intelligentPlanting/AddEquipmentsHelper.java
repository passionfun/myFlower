package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuaji.base.common.Factory;
import bocai.com.yanghuaji.model.BindEquipmentModel;
import bocai.com.yanghuaji.model.EquipmentModel;
import bocai.com.yanghuaji.model.LongToothRspModel;
import bocai.com.yanghuaji.util.DateUtils;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 *
 * Created by shc on 2017/12/27.
 */

public class AddEquipmentsHelper {
    private static final Gson gson = new Gson();
    private static boolean isBindSuccess = false;

    public static boolean startBind(final EquipmentModel mEquipmentModel) {
        final String timeStamp = DateUtils.getCurrentDateTimes();
        BindEquipmentModel model = new BindEquipmentModel("BR", timeStamp);
        final String request = gson.toJson(model);
        Log.d("sunhengchao", "startbind: " + request);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 6000);
        LongTooth.request(mEquipmentModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length,
                new SampleAttachment(), new MyLongToothServiceResponseHandler());
        return isBindSuccess;
    }


    static class MyLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        private boolean isRsp = false;

        public MyLongToothServiceResponseHandler() {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isRsp) {
                        isBindSuccess = false;
                    }
                }
            }, 6000);
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
            isRsp = true;
            Log.d("sunhengchao", "handleServiceResponse: " + new String(args));
            final LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
            if (longToothRspModel.getCODE() == 0) {
                isBindSuccess = true;
            } else {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        Factory.decodeRspCode(longToothRspModel);
                        isBindSuccess = false;
                    }
                });
            }
        }
    }


}
