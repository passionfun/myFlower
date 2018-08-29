package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.widget.Loading;

import org.eclipse.jetty.util.ajax.JSON;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuajien.base.RecyclerAdapter;
import bocai.com.yanghuajien.base.common.Factory;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.model.AddEquipmentsModel;
import bocai.com.yanghuajien.model.AutoModel;
import bocai.com.yanghuajien.model.AutoParaModel;
import bocai.com.yanghuajien.model.BindEquipmentModel;
import bocai.com.yanghuajien.model.EquipmentCard;
import bocai.com.yanghuajien.model.LedSetRspModel;
import bocai.com.yanghuajien.model.OldEquipmentInfo;
import bocai.com.yanghuajien.model.EquipmentInfoModel;
import bocai.com.yanghuajien.model.EquipmentModel;
import bocai.com.yanghuajien.model.EquipmentPhotoModel;
import bocai.com.yanghuajien.model.EquipmentRspModel;
import bocai.com.yanghuajien.model.LongToothRspModel;
import bocai.com.yanghuajien.model.MessageEvent;
import bocai.com.yanghuajien.model.PlantSeriesModel;
import bocai.com.yanghuajien.presenter.intelligentPlanting.AddEquipmentsContract;
import bocai.com.yanghuajien.presenter.intelligentPlanting.AddEquipmentsPresenter;
import bocai.com.yanghuajien.presenter.intelligentPlanting.AddEquipmentsRecylerContract;
import bocai.com.yanghuajien.presenter.intelligentPlanting.AddEquipmentsRecylerPresenter;
import bocai.com.yanghuajien.service.MyLongToothService;
import bocai.com.yanghuajien.ui.main.MainActivity;
import bocai.com.yanghuajien.util.ConstUtil;
import bocai.com.yanghuajien.util.DateUtils;
import bocai.com.yanghuajien.util.LogUtil;
import bocai.com.yanghuajien.util.LongToothUtil;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
import bocai.com.yanghuajien.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;
import io.fogcloud.sdk.easylink.api.EasyLink;
import io.fogcloud.sdk.easylink.helper.EasyLinkCallBack;
import io.fogcloud.sdk.easylink.helper.EasyLinkParams;
import io.fogcloud.sdk.mdns.api.MDNS;
import io.fogcloud.sdk.mdns.helper.SearchDeviceCallBack;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * 作者 yuanfei on 2017/12/8.
 * 邮箱 yuanfei221@126.com
 * 设备恢复配置调用的接口和设置参数：
 *             1、equipment_info
 *             2、plant_info
 *             3、Auto
 *             4、noDisturb
 *             5、lightOn
 */

public class AddEquipmentsActivity extends PresenterActivity<AddEquipmentsContract.Presenter>
        implements AddEquipmentsContract.View,MyLongToothService.MyLongToothListener {
    private static final String TAG = "AddEquipmentsActivity";
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.recycler)
    RecyclerView mRecyler;

    @BindView(R.id.tv_right)
    TextView mSave;

    @BindView(R.id.loading_add_equipments)
    Loading mLoadingAddEquipments;

    @BindView(R.id.tv_searchText)
    TextView tv_searchText;

    public static String KEY_PLANT_CARD = "KEY_PLANT_CARD";
    private String ssid;
    private String password;
    private String mToken = "";
    private String deviceUuid = "";
    private RecyclerAdapter<EquipmentModel> mAdapter;
    private Gson gson = new Gson();
    //搜索设备用
    //所有在线设备的系列名称集合
    List<String> series = new ArrayList<>();
    //用户账户下所有的设备列表
    private List<EquipmentRspModel.ListBean> frontPageDeviceList = new ArrayList<>();
    private List<EquipmentModel> newDeviceList = new ArrayList<>();
    private List<String> successLtidList = new ArrayList<>();
    //绑定失败老设备的列表
    private List<String> bindOldDeviceFailList = new ArrayList<>();
    //绑定失败新设备的列表
    private List<String> bindNewDeviceFailList = new ArrayList<>();
    //设置防打扰时间成功的设备列表
    private List<String> ndOldDeviceSuccessList = new ArrayList<>();
    //设置防打扰时间失败的设备列表
    private List<String> ndOldDeviceFailList = new ArrayList<>();
    //补光开启时间成功
    private List<String> lightOnOldDeviceSuccessList = new ArrayList<>();
    //补光开启时间失败
    private List<String> lightOldDeviceFailList = new ArrayList<>();
    //老设备智能控制发送成功
    private List<String> autoOldDeviceSuccessList = new ArrayList<>();
    //老设备智能控制发送失败
    private List<String> autoOldDeviceFailList = new ArrayList<>();
    //新设备的操作列表
    private List<Integer> opStateNewList = new ArrayList<>();
    //老设备的操作列表
    private List<Integer> opStateOldList = new ArrayList<>();
    private PlantSeriesModel.PlantSeriesCard plantSeriesCard;
    //被选中要添加的设备集合
    private List<EquipmentModel> equipmentModels = new ArrayList<>();
    private List<OldEquipmentInfo> oldEquipmentInfoList = new ArrayList<>();
    private List<OldEquipmentInfo> tempOldDevice = new ArrayList<>();
    private Timer restoreTime = null;
    private Timer rbTimer = null;
    private boolean isRbTimerRun = false;
    private boolean isRestore = true;
    private EquipmentInfoModel equipmentInfoModel = null;
    private OldEquipmentInfo oldEquipmentInfo = null;
    MDNS mdns = new MDNS(this);
    EasyLink elink = new EasyLink(this);
    private String pid = "";
    private int newDeviceSize = 0;
    private int oldDeviceSize = 0;
    private int restoreSuccessSize = 0;
    private int addSuccessSize = 0;
    private MyLongtoothHandler longthToothHandler = new MyLongtoothHandler(this);
    @Override
    public void updateUI(Message msg) {
        longthToothHandler.sendMessage(msg);
    }

    @Override
    public void timeOutMsg(Message msg) {
        longthToothHandler.sendMessage(msg);
    }

    private int str2Sec(String timeStr){
        if(!TextUtils.isEmpty(timeStr)){
            if(timeStr.contains(":")){
                String[] st = timeStr.split(":");
                int hour = Integer.parseInt(st[0]);
                int min = Integer.parseInt(st[1]);
                return hour * 60 * 60 + min * 60;
            }else{
                LogUtil.d(TAG,"str2Sec have not contain(:)");
                return 0;
            }
        }else{
            LogUtil.d(TAG,"str2Sec is null");
            return 0;
        }
    }
    private class MyLongtoothHandler extends Handler{
        private WeakReference<AddEquipmentsActivity> addEquipmentsActivityWeakReference = null;
        private MyLongtoothHandler(AddEquipmentsActivity activity){
            addEquipmentsActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AddEquipmentsActivity addEquipmentsActivity = addEquipmentsActivityWeakReference.get();
            if(addEquipmentsActivity == null){
                return;
            }
            LogUtil.d(TAG,"*啦啦啦啦啦啦啦哈哈哈哈*长牙通信有返回数据啦**********longtooth handler handleMessage:"+msg.obj.toString());
            try {
                String successLtid = "";
                String msgRes = msg.obj.toString();
                int code = -1;
                if(msgRes.contains("==")){
                    LogUtil.d(TAG,"收到消息了，并且有执行成功的ltid,消息中含有==");
                    String[] bindResArr = msgRes.split("==");
                    JSONObject joBindRes = new JSONObject(bindResArr[0]);
                    successLtid = bindResArr[1];
                    code = joBindRes.getInt("CODE");
                }
                switch (msg.what){
                    case ConstUtil.TIME_OUT:
                        LogUtil.d(TAG,"time out &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                        Application.showToast(Application.getStringText(R.string.equipment_not_response_save_water_set_failed));
                        break;
                    case ConstUtil.BIND_DEVICE_SUCCESS://0、重新绑定老设备的状态码；1、绑定新设备的状态码；2、恢复老设备的配置的状态码
                        List<AddEquipmentsModel> models = new ArrayList<>();
                            if(code == 0){
                                if(newDeviceSize > 0){
                                    if(oldDeviceSize > 0){
                                        //新设备
                                        LogUtil.d(TAG,"新老设备都有啦，绑定成功啦…………………………………………");
                                        for(int i = 0 ;i < newDeviceSize;i++){
                                            if(successLtid.equals(newDeviceList.get(i).getLTID())){
                                                if(opStateNewList.get(i) == 1){
                                                    LogUtil.d(TAG,"newDevice bind success"+successLtid);

                                                    String mEquipmentName = newDeviceList.get(i).getDEVNAME();
                                                    String macAddress = newDeviceList.get(i).getMAC();
                                                    String serialNum = "";
                                                    String version = newDeviceList.get(i).get_$FirmwareRev196();
                                                    String series = mEquipmentName.substring(0, 5);
                                                    String dLtid = newDeviceList.get(i).getLTID();
                                                    AddEquipmentsModel model = new AddEquipmentsModel(mEquipmentName, macAddress, serialNum, version, dLtid
                                                            , deviceUuid, series);
                                                    models.add(model);
                                                    mPresenter.addEquipments(mToken, gson.toJson(models));
                                                }else{
                                                    LogUtil.d(TAG,"handler newDevice bind other");
                                                }
                                            }
                                        }
                                        //老设备
                                        for(int j = 0;j < oldDeviceSize;j++){
                                            if(successLtid.equals(oldEquipmentInfoList.get(j).getLtid())){
                                                if(opStateOldList.get(j) == 0){//rebind
                                                    LogUtil.d(TAG,"olddevice rebind success:"+successLtid);
                                                    String mEquipmentName = oldEquipmentInfoList.get(j).getDeviceName();
                                                    String macAddress = oldEquipmentInfoList.get(j).getMac();
                                                    String serialNum = "";
                                                    String version = oldEquipmentInfoList.get(j).getVersion();
                                                    String series = mEquipmentName.substring(0, 5);
                                                    String dLtid = oldEquipmentInfoList.get(j).getLtid();
                                                    AddEquipmentsModel model = new AddEquipmentsModel(mEquipmentName, macAddress, serialNum, version, dLtid
                                                            , deviceUuid, series);
                                                    models.add(model);
                                                    mPresenter.addEquipments(mToken, gson.toJson(models));
                                                }else{//2 restore
                                                    LogUtil.d(TAG,"olddevice old uuid rebind success:"+successLtid);
                                                    String deviceId = oldEquipmentInfoList.get(j).getId();
                                                    Map<String, String> restoreMap = new HashMap<>();
                                                    restoreMap.put("Token", mToken);
                                                    restoreMap.put("Id", deviceId);
                                                    LogUtil.d(TAG,"开始调用equipment_info接口(首页mac)和绑定成功的ltid相等,恢复配置->token:"+mToken+",id:"+deviceId);
                                                    mPresenter.equipmentInfo(restoreMap);//equipment_info
                                                }
                                            }
                                        }
                                    }else{
                                        //只有新设备
                                        LogUtil.d(TAG,"只有新设备啦，并且绑定成功啦………………………………………………………………………………");
                                        for(int i = 0 ;i < newDeviceSize;i++){
                                            if(successLtid.equals(newDeviceList.get(i).getLTID())){
                                                if(opStateNewList.get(i) == 1){
                                                    LogUtil.d(TAG,"newDevice bind success and start addDevice to server+"+successLtid);
                                                    String mEquipmentName = newDeviceList.get(i).getDEVNAME();
                                                    String macAddress = newDeviceList.get(i).getMAC();
                                                    String serialNum = "";
                                                    String version = newDeviceList.get(i).get_$FirmwareRev196();
                                                    String series = mEquipmentName.substring(0, 5);
                                                    String dLtid = newDeviceList.get(i).getLTID();
                                                    AddEquipmentsModel model = new AddEquipmentsModel(mEquipmentName, macAddress, serialNum, version, dLtid
                                                            , deviceUuid, series);
                                                    models.add(model);
                                                    mPresenter.addEquipments(mToken, gson.toJson(models));
                                                }else{
                                                    LogUtil.d(TAG,"newDevice bind****************************** ");
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    //只有老设备
                                    LogUtil.d(TAG,"只有老设备啦，绑定成功啦………………………………………………………………");
                                    if(oldDeviceSize > 0){
                                        for(int j = 0;j < oldDeviceSize;j++){
                                            if(successLtid.equals(oldEquipmentInfoList.get(j).getLtid())){
                                                if(opStateOldList.get(j) == 0){//rebind
                                                    LogUtil.d(TAG,"only olddevice rebind success:"+successLtid);
                                                    String mEquipmentName = oldEquipmentInfoList.get(j).getDeviceName();
                                                    String macAddress = oldEquipmentInfoList.get(j).getMac();
                                                    String serialNum = "";
                                                    String version = oldEquipmentInfoList.get(j).getVersion();
                                                    String series = mEquipmentName.substring(0, 5);
                                                    String dLtid = oldEquipmentInfoList.get(j).getLtid();
                                                    AddEquipmentsModel model = new AddEquipmentsModel(mEquipmentName, macAddress, serialNum, version, dLtid
                                                            , deviceUuid, series);
                                                    models.add(model);
                                                    mPresenter.addEquipments(mToken, gson.toJson(models));
                                                }else{//restore
                                                    LogUtil.d(TAG,"olddevice rebind old uuid success:"+successLtid);
                                                    String deviceId = oldEquipmentInfoList.get(j).getId();
                                                    Map<String, String> restoreMap = new HashMap<>();
                                                    restoreMap.put("Token", mToken);
                                                    restoreMap.put("Id", deviceId);
                                                    LogUtil.d(TAG,"只有老设备，调用equipment_info,restore(首页mac)和绑定成功的ltid相等,恢复配置->token:"+mToken+",id:"+deviceId);
                                                    mPresenter.equipmentInfo(restoreMap);
                                                }
                                            }
                                        }
                                    }
                                }
                            }else{
                                LogUtil.d(TAG,"handler Bind fail code()U()(绑定失败（老设备或者新设备）)()))))())(@#￥%……&……&（*)(:"+code+",ltid:"+successLtid);
                                if(oldDeviceSize > 0){
                                    //既有新设备又有老设备
                                    if(newDeviceSize > 0){
                                        //老设备
                                        for (int i = 0;i < oldDeviceSize;i++){
                                            if(successLtid.equals(oldEquipmentInfoList.get(i).getLtid())){
                                                if(opStateOldList.get(i) == 0 || opStateOldList.get(i) == 2){//重新绑定(新的uuid绑定设备失败，老的uuid绑定)
                                                    if(!bindOldDeviceFailList.contains(successLtid)){
                                                        bindOldDeviceFailList.add(successLtid);
                                                    }
                                                }
                                            }
                                        }
                                        //新设备
                                        for(int j = 0;j < newDeviceSize;j++){
                                            if(successLtid.equals(newDeviceList.get(j).getLTID())){
                                                if(opStateNewList.get(j) == 1){
                                                    if(!bindNewDeviceFailList.contains(successLtid)){
                                                        bindNewDeviceFailList.add(successLtid);
                                                    }
                                                }
                                            }
                                        }

                                        int newFailSize = bindNewDeviceFailList.size();
                                        int oldFailSize = bindOldDeviceFailList.size();
                                        //新老设备全部绑定失败
                                        if(newFailSize + oldFailSize == oldDeviceSize + newDeviceSize){
                                            LogUtil.d(TAG,"新老设备全部绑定失败(error code:11)");
                                            hideLoading();
                                            Application.showToast(R.string.all_devices_bind_fail+"(error code:11)");
                                        }else{
                                            LogUtil.d(TAG,"新老设备中有部分设备绑定失败，后面有处理，此处不处理");
                                        }

                                    }else{
                                        //只有老设备
                                        for (int i = 0;i < oldDeviceSize;i++){
                                            if(successLtid.equals(oldEquipmentInfoList.get(i).getLtid())){
                                                if(opStateOldList.get(i) == 0 || opStateOldList.get(i) == 2){//重新绑定(新的uuid绑定设备失败，老的uuid绑定)
                                                    if(!bindOldDeviceFailList.contains(successLtid)){
                                                        bindOldDeviceFailList.add(successLtid);
                                                    }
                                                }
                                            }
                                        }
                                        if(newDeviceSize == 0 && bindOldDeviceFailList.size() == oldDeviceSize){
                                            LogUtil.d(TAG,"只有老设备，所有的老设备绑定失败（error code:10）");
                                            hideLoading();
                                            Application.showToast(R.string.all_devices_bind_fail+"(error code:10)");
                                        }else{
                                            LogUtil.d(TAG,"只有老设备，部分老设备绑定失败，后面有处理，此处不处理");
                                        }
                                    }
                                }else{
                                    //只有新设备
                                    if(newDeviceSize > 0){
                                        for (int i = 0;i < newDeviceSize;i++){
                                            if(successLtid.equals(newDeviceList.get(i).getLTID())){
                                                if(opStateNewList.get(i) == 1){
                                                    if(!bindNewDeviceFailList.contains(successLtid)){
                                                        bindNewDeviceFailList.add(successLtid);
                                                    }
                                                }
                                            }
                                        }
                                        if(oldDeviceSize == 0 && newDeviceSize == bindNewDeviceFailList.size()){
                                            LogUtil.d(TAG,"只有新设备，所有的新设备绑定失败（error code:12）");
                                            hideLoading();
                                            Application.showToast(R.string.all_devices_bind_fail+"(error code:12)");
                                        }else{
                                            LogUtil.d(TAG,"只有新设备，部分新设备绑定失败，后面有处理，此处不处理");
                                        }
                                    }
                                }
                            }
                        break;

                    case ConstUtil.SMART_CONTROL_AUTO_SUCCESS://智能控制成功（Auto）
                        if(code == 0){
                            LogUtil.d(TAG,"(restore 3)Auto SUCCESS:"+successLtid);
                            //发送noDisturb命令
                            JSONObject noDisturbJo = null;
                            int beginSec = str2Sec(equipmentInfoModel.getBanStart());
                            int endSec = str2Sec(equipmentInfoModel.getBanStop());
                            noDisturbJo = new JSONObject();
                            noDisturbJo.put("CMD",ConstUtil.CMD_DEVICE_NODISTURB);
                            noDisturbJo.put("UUID",oldEquipmentInfo.getUuid());
                            noDisturbJo.put("Begin",beginSec);
                            noDisturbJo.put("End",endSec);
                            String noDisturbCMD = String.valueOf(noDisturbJo);
                            LogUtil.d(TAG,"发送noDisturb命令的json字符串："+noDisturbCMD);
                            MyLongToothService.sendDataFrame(AddEquipmentsActivity.this,ConstUtil.OP_NODISTURB,oldEquipmentInfo.getLtid(),noDisturbCMD);
                            MyLongToothService.setMyLongToothListener(AddEquipmentsActivity.this);
                        }else{
                            LogUtil.d(TAG,"(restore 3)Auto Fail:"+successLtid);
                            if(oldDeviceSize > 0){
                                for(int i = 0;i < oldDeviceSize;i++){
                                    if(successLtid.equals(oldEquipmentInfoList.get(i).getLtid())){
                                        if(!autoOldDeviceFailList.contains(successLtid)){
                                            autoOldDeviceFailList.add(successLtid);
                                        }
                                    }
                                }
                                if(oldDeviceSize == autoOldDeviceFailList.size()){
                                    LogUtil.d(TAG,"所有的老设备发送Auto命令失败（error code：30）");
                                    hideLoading();
                                    Application.showToast(R.string.old_devices_send_auto_fail+"(error code:30)");
                                }else{
                                    LogUtil.d(TAG,"部分老设备发送Auto命令失败，此处不处理，后面有处理");
                                }
                            }
                        }

                        break;
                    case ConstUtil.SMART_CONTROL_AUTO_FAIL://智能控制失败（Auto）
                        LogUtil.d(TAG,"AUTO FAIL");
                        break;
                    case ConstUtil.NO_DISTURB_SUCCESS://设置免打扰时间成功（noDisturb）
                        if(code == 0){
                            LogUtil.d(TAG,"(restore 4)noDisturb SUCCESS:"+successLtid);
                            //发送lightOn命令
                            int begin = str2Sec(equipmentInfoModel.getLightStart());
                            JSONObject lightonJson = new JSONObject();
                            lightonJson.put("CMD",ConstUtil.CMD_LIGHT_ON);
                            lightonJson.put("UUID",oldEquipmentInfo.getUuid());
                            lightonJson.put("Begin",begin);
                            String lightOnCMD = String.valueOf(lightonJson);
                            LogUtil.d(TAG,"发送设置补光开启时间的数据帧："+lightOnCMD);
                            MyLongToothService.sendDataFrame(AddEquipmentsActivity.this,ConstUtil.OP_LIGHTON,oldEquipmentInfo.getLtid(),lightOnCMD);
                            MyLongToothService.setMyLongToothListener(AddEquipmentsActivity.this);
                        }else{
                            LogUtil.d(TAG,"(restore 4)noDisturb Fail:"+successLtid);
                            if(oldDeviceSize > 0){
                                for(int i = 0;i < oldDeviceSize;i++){
                                    if(successLtid.equals(oldEquipmentInfoList.get(i).getLtid())){
                                        if(!ndOldDeviceFailList.contains(successLtid)){
                                            ndOldDeviceFailList.add(successLtid);
                                        }
                                    }
                                }
                                if(oldDeviceSize == ndOldDeviceFailList.size()){
                                    LogUtil.d(TAG,"所有的老设备发送noDisturb命令失败（error code：40）");
                                    hideLoading();
                                    Application.showToast(R.string.old_devices_send_auto_fail+"(error code:40)");
                                }else{
                                    LogUtil.d(TAG,"部分老设备发送noDisturb命令失败，此处不处理，后面有处理");
                                }
                            }
                        }

                        break;
                    case ConstUtil.NO_DISTURB_FAIL://设置免打扰时间失败（noDisturb）
                        LogUtil.d(TAG,"NODISTURB FAIL");
                        break;
                    case ConstUtil.LIGHT_ON_SUCCESS:
                        if(code == 0){
                            LogUtil.d(TAG,"(restore 5)LightOn SUCCESS:"+successLtid);
                            LogUtil.d(TAG,"LIGHTON SUCCESS");
                            restoreSuccessSize++;
                            LogUtil.d(TAG,"lightOn的大小（恢复配置最后一步发送的数据帧）:"+restoreSuccessSize);
                            if(newDeviceSize == 0){
                                if (restoreSuccessSize == oldDeviceSize) {
                                    hideLoading();
                                    MainActivity.show(AddEquipmentsActivity.this);
                                    finish();
                                }
                            }else{
                                //既有老设备又有新设备的情况下：新设备添加到服务器成功的数量+老设备恢复配置成功的设备的数量 = 新设备的总数量 +  老设备的总数量
                                LogUtil.d(TAG,"既有老设备又有新设备"+"addSucessSize:"+addSuccessSize+"+"+restoreSuccessSize+"=newDeviceSize:"+newDeviceSize+"+oldDeviceSize:"+oldDeviceSize);
                                if(addSuccessSize + restoreSuccessSize == newDeviceSize + oldDeviceSize){
                                    hideLoading();
                                    MainActivity.show(AddEquipmentsActivity.this);
                                    finish();
                                }else{
                                    if(isRestore){
                                        isRestore = false;
                                        if(restoreTime == null){
                                            restoreTime = new Timer();
                                            LogUtil.d(TAG,"部分老设备恢复成功，定时器5s后启动");
                                            restoreTime.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    LogUtil.d(TAG,"部分老设备恢复成功，定时器启动啦");
                                                    hideLoading();
                                                    MainActivity.show(AddEquipmentsActivity.this);
                                                    finish();
                                                }
                                            },5000L,5000L);
                                        }

                                    }
                                }
                            }
                        }else{
                            LogUtil.d(TAG,"(restore 5)LightOn Fail:"+successLtid);
                            if(oldDeviceSize > 0){
                                for(int i = 0;i < oldDeviceSize;i++){
                                    if(successLtid.equals(oldEquipmentInfoList.get(i).getLtid())){
                                        if(!lightOldDeviceFailList.contains(successLtid)){
                                            lightOldDeviceFailList.add(successLtid);
                                        }
                                    }
                                }
                                if(oldDeviceSize == lightOldDeviceFailList.size()){
                                    LogUtil.d(TAG,"所有的老设备发送LigthOn命令失败（error code：50）");
                                    hideLoading();
                                    Application.showToast(R.string.old_devices_send_auto_fail+"(error code:50)");
                                }else{
                                    LogUtil.d(TAG,"部分老设备发送LigthOn命令失败，此处不处理，后面有处理");
                                    if(isRestore){
                                        isRestore = false;
                                        if(restoreTime == null){
                                            restoreTime = new Timer();
                                            LogUtil.d(TAG,"部分老设备恢复成功，定时器restoreTime初始化啦，5s后启动……………………………………………………………………………………………………");
                                            restoreTime.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    LogUtil.d(TAG,"部分老设备恢复成功，定时器启动啦#######################################################################");
                                                    hideLoading();
                                                    MainActivity.show(AddEquipmentsActivity.this);
                                                    finish();
                                                }
                                            },5000L,5000L);
                                        }

                                    }
                                }
                            }
                        }
                        break;
                    case ConstUtil.LIGHT_ON_FAIL:
                        LogUtil.d(TAG,"LIGHT FAILED");
                        break;
                        default:break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtil.d(TAG,"JSONException :"+e.getMessage());
            }
        }
    }

    //显示的入口
    public static void show(Context context, String ssid, String password, PlantSeriesModel.PlantSeriesCard plantSeriesCard) {
        Intent intent = new Intent(context, AddEquipmentsActivity.class);
        intent.putExtra(KEY_PLANT_CARD, plantSeriesCard);
        intent.putExtra(ConnectActivity.KEY_SSID, ssid);
        intent.putExtra(ConnectActivity.KEY_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_equipments;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        ssid = bundle.getString(ConnectActivity.KEY_SSID);
        password = bundle.getString(ConnectActivity.KEY_PASSWORD);
        plantSeriesCard = (PlantSeriesModel.PlantSeriesCard) bundle.getSerializable(KEY_PLANT_CARD);
        return super.initArgs(bundle);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }


    @OnClick(R.id.tv_right)
    void onConfirmClick() {
        //点击开始绑定停止mdns扫描
        mdns.stopSearchDevices(null);
        elink.stopEasyLink(null);
//        if (isSaveClicked){
//            Application.showToast("请返回重新添加");
//            return;
//        }
//        showLoading();
        bindEquipments();
    }

    private void bindEquipments(){
        if (equipmentModels == null || equipmentModels.size() == 0) {
            hideLoading();
            Application.showToast(Application.getStringText(R.string.please_select_equipment_to_add));
            return;
        }
        //清除原来列表中的新设备
        if(newDeviceList != null && newDeviceList.size() > 0){
            LogUtil.d(TAG,"新设备不为空，清空列表");
            newDeviceList.clear();
            newDeviceList = new ArrayList<>();
        }

        //清除原来的老设备
        if(oldEquipmentInfoList != null && oldEquipmentInfoList.size() > 0){
            LogUtil.d(TAG,"老设备不为空，清空列表");
            oldEquipmentInfoList.clear();
            oldEquipmentInfoList = new ArrayList<>();
        }

        final List<String> frontPageDeviceNameList = new ArrayList<>();
        int bindDeviceSize = equipmentModels.size();
        int frontPageDeviceSize = frontPageDeviceList.size();

        LogUtil.d(TAG, "选中绑定的设备的台数:" + bindDeviceSize + ",首页设备的台数:" + frontPageDeviceSize);
        boolean hasBeforeDevice = false;
        int frontPageIndex = -1;
        for (int i = 0; i < bindDeviceSize; i++) {//选中绑定的设备
            String bindMac = equipmentModels.get(i).getMAC();
            for (int j = 0; j < frontPageDeviceSize; j++) {//首页的设备
                //扫描到设备的mac和首页的mac相等并且植物的名称不为空（添加过植物才是旧设备），即为旧设备
                String frontpageMac = frontPageDeviceList.get(j).getMac();
                String frontpagePlantName = frontPageDeviceList.get(j).getPlantName();
                if (!TextUtils.isEmpty(frontpagePlantName) && bindMac.equals(frontpageMac)) {
                    LogUtil.d(TAG, "have front page device===============================================");
                    //之前配置过该设备
                    hasBeforeDevice = true;
                    frontPageIndex = j;
                    break;
                } else {
                    //新设备
                    LogUtil.d(TAG, "have new device···················································");
                    hasBeforeDevice = false;
                }
            }

            if (hasBeforeDevice) {
                //有之前的设备
                oldEquipmentInfo = new OldEquipmentInfo(frontPageDeviceList.get(frontPageIndex).getEquipName(),frontPageDeviceList.get(frontPageIndex).getPSIGN(),
                                                          frontPageDeviceList.get(frontPageIndex).getId(),frontPageDeviceList.get(frontPageIndex).getMac(),
                                                            frontPageDeviceList.get(frontPageIndex).getLTID(),equipmentModels.get(i).get_$FirmwareRev196());
                oldEquipmentInfoList.add(oldEquipmentInfo);
                tempOldDevice.add(oldEquipmentInfo);
                frontPageDeviceNameList.add(frontPageDeviceList.get(frontPageIndex).getEquipName());
            }else{
                newDeviceList.add(equipmentModels.get(i));
            }
        }

        //有之前的设备才弹出对话框提示用户
        //1、老设备。2、老设备和新设备
        newDeviceSize = newDeviceList.size();
        oldDeviceSize = oldEquipmentInfoList.size();

        LogUtil.d(TAG,"添加的新设备的数量为："+newDeviceSize+",info:"+new Gson().toJson(newDeviceList));
        LogUtil.d(TAG,"老设备的数量为："+oldDeviceSize+",info:"+new Gson().toJson(oldEquipmentInfoList));

        if(oldDeviceSize > 0){
            final AlertDialog.Builder bindDialog = new AlertDialog.Builder(this);
            int nameSize = frontPageDeviceNameList.size();
            String[] deviceNameArr = new String[nameSize];
            for (int k = 0 ;k < frontPageDeviceNameList.size();k++){
                deviceNameArr[k] = frontPageDeviceNameList.get(k);
            }
            LogUtil.d(TAG,"老设备的名称列表名称："+new Gson().toJson(deviceNameArr)+",大小："+deviceNameArr.length+",老设备的列表信息："+new Gson().toJson(oldEquipmentInfoList));
            boolean[] checkItems = new boolean[deviceNameArr.length];
            for(int a = 0;a < checkItems.length;a++){
                checkItems[a] = true;
            }
            if(oldDeviceSize == 1){
                bindDialog.setTitle(Application.getStringText(R.string.tip_chose_bind_device_single));
            }else{
                bindDialog.setTitle(Application.getStringText(R.string.tip_chose_bind_device_multiple));
            }
            bindDialog.setMultiChoiceItems(deviceNameArr, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    LogUtil.d(TAG,"是否选中复选框：" + isChecked+",which:"+which);
                    int tempSize = tempOldDevice.size();
                    for(int i = 0;i < tempSize;i++){
                        if(tempOldDevice.get(i).getDeviceName().equals(tempOldDevice.get(which).getDeviceName())){
                            if(isChecked){
                                oldEquipmentInfoList.add(tempOldDevice.get(i));
                                LogUtil.d(TAG,"选中的设备位置："+which+",信息："+new Gson().toJson(oldEquipmentInfoList));
                            }else{
                                oldEquipmentInfoList.remove(tempOldDevice.get(i));
                                LogUtil.d(TAG,"未选中的设备位置："+which+",信息："+new Gson().toJson(oldEquipmentInfoList));
                            }
                        }
                    }
                    oldDeviceSize = oldEquipmentInfoList.size();
                    LogUtil.d(TAG,"操作复选框后的老设备："+oldDeviceSize+"台-->"+new Gson().toJson(oldEquipmentInfoList));
                }
            }).setPositiveButton("ReBind", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    oldDeviceSize = oldEquipmentInfoList.size();
                    if(oldDeviceSize == 0 && newDeviceSize == 0){
                        LogUtil.d(TAG,"Rebind old and new all null");
                        Application.showToast(R.string.bind_device_null_text);
                        return;
                    }
                    showLoading();
                    isRbTimerRun = true;
                    initRbTimer();
                    deviceUuid = DateUtils.getCurrentDateTimes();
                    LogUtil.d(TAG,"Rebind uuid："+deviceUuid);
                    //重新绑定的操作
                    //1、只有老设备
                    if(newDeviceList.size() == 0){
                        LogUtil.d(TAG,"只有老设备，重新绑定:"+ deviceUuid);
                        for(int m = 0;m < oldEquipmentInfoList.size();m++){
                            startBind(oldEquipmentInfoList.get(m).getLtid(),deviceUuid);
                            opStateOldList.add(0);//重新绑定老设备的状态码
                        }
                    }else{
                        //2、既有老设备又有新设备
                        LogUtil.d(TAG,"既有老设备又有新设备，重新绑定："+deviceUuid);
                        for(int m = 0;m < oldEquipmentInfoList.size();m++){
                            startBind(oldEquipmentInfoList.get(m).getLtid(),deviceUuid);
                            opStateOldList.add(0);//重新绑定老设备的状态码
                        }
                        for(int n = 0;n < newDeviceList.size();n++){
                            startBind(newDeviceList.get(n).getLTID(),deviceUuid);
                            opStateNewList.add(1);//绑定新设备的状态码
                        }
                    }
                }
            }).setNegativeButton("Restore", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //恢复配置（老设备恢复配置），新设备绑定操作
                    oldDeviceSize = oldEquipmentInfoList.size();
                    if(oldDeviceSize == 0 && newDeviceSize == 0){
                        LogUtil.d(TAG,"Restore old and new all null");
                        Application.showToast(R.string.restore_device_null_text);
                        return;
                    }
                    showLoading();
                    isRbTimerRun = true;
                    initRbTimer();
                    deviceUuid = DateUtils.getCurrentDateTimes();
                    LogUtil.d(TAG,"Restore uuid："+deviceUuid);
                    //1、只有老设备
                    if(newDeviceList.size() == 0){
                        LogUtil.d(TAG,"恢复配置，只有老的设备"+deviceUuid+",老设备的size："+oldDeviceSize);
                        for(int m = 0;m < oldDeviceSize;m++){
                            startBind(oldEquipmentInfoList.get(m).getLtid(),oldEquipmentInfoList.get(m).getUuid());
                            opStateOldList.add(2);//恢复老设备的配置的状态码
                        }
                    }else{
                        LogUtil.d(TAG,"恢复配置，老设备恢复配置，新设备绑定");
                        //2、既有老设备又有新设备
                        for(int m = 0; m < oldEquipmentInfoList.size();m++){
                            startBind(oldEquipmentInfoList.get(m).getLtid(),oldEquipmentInfoList.get(m).getUuid());
                            opStateOldList.add(2);//恢复老设备的配置的状态码
                        }
                        for(int n = 0;n < newDeviceList.size();n++){
                            startBind(newDeviceList.get(n).getLTID(),deviceUuid);
                            opStateNewList.add(1);//绑定新设备的状态码
                        }
                    }
                }
            });
            bindDialog.create().show();
        }else{
            //只有新设备
            showLoading();
            deviceUuid = DateUtils.getCurrentDateTimes();
            LogUtil.d(TAG,"只有新设备绑定:"+deviceUuid);
            for(int n = 0;n < newDeviceList.size();n++){
                startBind(newDeviceList.get(n).getLTID(),deviceUuid);
                opStateNewList.add(1);//绑定新设备的状态码
            }
        }
    }
    public void initRbTimer(){
        if(isRbTimerRun){
            isRbTimerRun = false;
            if(rbTimer == null){
                LogUtil.d(TAG,"rbTimer初始化啦，30s后启动******************************************************************************");
                rbTimer = new Timer();
                rbTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        LogUtil.d(TAG,"操作执行超过了30s，执行失败");
                        hideLoading();
                        Application.showToast(R.string.server_timeout_exp);
                        if(rbTimer != null){
                            LogUtil.d(TAG,"rbTimer关闭");
                            rbTimer.cancel();
                            rbTimer = null;
                        }
                    }
                },30000L,5000L);
            }
        }

    }
    public void startBind(String ltid,String uuid) {
        BindEquipmentModel model = new BindEquipmentModel("BR", uuid);
        final String request = gson.toJson(model);
        LogUtil.d(TAG, "发送绑定的CMD:" + request+",ltid:"+ltid);
        MyLongToothService.sendDataFrame(AddEquipmentsActivity.this,ConstUtil.OP_BIND,ltid,request);
        MyLongToothService.setMyLongToothListener(AddEquipmentsActivity.this);
    }



    class MyLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        private boolean isRsp = false;
        private List<AddEquipmentsModel> models;
        private EquipmentModel equipmentModel;
        private String timeStamp;

        public MyLongToothServiceResponseHandler(List<AddEquipmentsModel> models, final EquipmentModel equipmentModel, String timeStamp) {
            this.models = models;
            this.equipmentModel = equipmentModel;
            this.timeStamp = timeStamp;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isRsp) {
                        LogUtil.d(TAG,"绑定超时时间3s："+"No response to the device binding");
                        Application.showToast(R.string.bind_no_response);
//                        EventBus.getDefault().post(new MessageEvent(equipmentModel.getLTID(),MessageEvent.FAILED));
                    }
                }
            }, 3000L);
        }

        @Override
        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                          String service_str, int data_type, byte[] args,
                                          LongToothAttachment attachment) {
            if (args == null)
                return;
            String result = new String(args);
            if (TextUtils.isEmpty(result) || !result.contains("CODE")) {
                return;
            }
            isRsp = true;
            LogUtil.d(TAG, "(绑定设备的回调)handleServiceResponse(ltid): " + ltid_str + "--->" + result);
            final LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
            if (longToothRspModel.getCODE() == 0) {
                String mEquipmentName = equipmentModel.getDEVNAME();
                String macAddress = equipmentModel.getMAC();
                String serialNum = "";
                String version = equipmentModel.get_$FirmwareRev196();
                String series = mEquipmentName.substring(0, 5);
                AddEquipmentsModel model = new AddEquipmentsModel(mEquipmentName, macAddress, serialNum, version, equipmentModel.getLTID()
                        , timeStamp, series);
                models.add(model);
            } else {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        EventBus.getDefault().post(new MessageEvent(equipmentModel.getLTID(), MessageEvent.FAILED));
                        Factory.decodeRspCode(longToothRspModel);
                    }
                });
            }
        }
    }
    private Timer addTimer = null;
    private boolean isAddSuccess = false;
    @Override
    public void addEquipmentsSuccess(List<EquipmentCard> cards) {
        LogUtil.d(TAG,"addEquipmentsSuccess："+new Gson().toJson(cards));
        //通知主页面刷新数据
//        EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
//        EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
        for (EquipmentCard card : cards) {
            String mLtid = card.getLTID();
            EventBus.getDefault().post(new MessageEvent(mLtid, MessageEvent.SUCCESS));
            successLtidList.add(mLtid);
        }
        //1、只有老设备 2、老设备和新设备都有
        int sumSize = oldDeviceSize + newDeviceSize;
        addSuccessSize = successLtidList.size();
        LogUtil.d(TAG,"addEquipmentsSuccess (successltidsize/newdeviceSize/olddevicesize/sumsize):"+"successltidsize="+addSuccessSize+",newdeviceSize="+newDeviceSize+",olddevicesize="+oldDeviceSize+",sumsize="+sumSize);
        //只有新设备
        if(oldDeviceSize == 0){
            LogUtil.d(TAG,"只有新设备添加成功了");
            if(addSuccessSize == newDeviceSize){
                LogUtil.d(TAG,"只有新设备，addSuccessSize："+addSuccessSize+",newDeviceSize:"+newDeviceSize);
                hideLoading();
                EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
                EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
                MainActivity.show(AddEquipmentsActivity.this);
                finish();
            }else{
                if(addTimer == null){
                    addTimer = new Timer();
                    LogUtil.d(TAG,"只有新设备addTimer初始化啦，5s之后启动哈………………………………………………………………………………………………");
                    if(!isAddSuccess){
                        isAddSuccess = true;
                        addTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                LogUtil.d(TAG,"只有新设备addTimer 启动啦#####################################################################");
                                hideLoading();
                                EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
                                EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
                                MainActivity.show(AddEquipmentsActivity.this);
                                finish();
                            }
                        },5000L,5000L);
                    }
                }
            }
        }else{
            if(newDeviceSize > 0){//新老设备都有
                if(sumSize == addSuccessSize){
                    LogUtil.d(TAG,"新老设备都有，addSuccessSize："+addSuccessSize+",newDeviceSize:"+newDeviceSize+",oldDeviceSize:"+oldDeviceSize);
                    hideLoading();
                    EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
                    EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
                    MainActivity.show(AddEquipmentsActivity.this);
                    finish();
                }else{
                    if(addTimer == null){
                        addTimer = new Timer();
                        LogUtil.d(TAG,"新老设备都有addTimer初始化了，5s之后启动哈…………………………………………………………………………………………………………");
                        if(!isAddSuccess){
                            isAddSuccess = true;
                            addTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    LogUtil.d(TAG,"新老设备都有addTimer 启动啦#####################################################################");
                                    hideLoading();
                                    EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
                                    EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
                                    MainActivity.show(AddEquipmentsActivity.this);
                                    finish();
                                }
                            },5000L,5000L);

                        }
                    }

                }
            }else{
                //只有老设备
                if(addSuccessSize == oldDeviceSize){
                    LogUtil.d(TAG,"只有老设备，addSuccessSize："+addSuccessSize+",oldDeviceSize:"+oldDeviceSize);
                    hideLoading();
                    EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
                    EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
                    MainActivity.show(AddEquipmentsActivity.this);
                    finish();
                }else{
                    if(addTimer == null){
                        addTimer = new Timer();
                        LogUtil.d(TAG,"只有老设备addTimer初始化啦，5s之后启动哈……………………………………………………………………………………………………");
                        if(!isAddSuccess){
                            isAddSuccess = true;
                            addTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    LogUtil.d(TAG,"只有老设备addTimer 启动啦#####################################################################");
                                    hideLoading();
                                    EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
                                    EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
                                    MainActivity.show(AddEquipmentsActivity.this);
                                    finish();
                                }
                            },5000L,5000L);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addEquipmentsFailed() {
        LogUtil.d(TAG,"addEquipmentsFailed");
    }

    @Override
    public void equipmentInfoSuccess(EquipmentInfoModel equipmentInfoModel) {
        //成功获取equipment_info的数据
        LogUtil.d(TAG,"(restore 1:equipmentinfo success)equipmentInfoSuccess:"+new Gson().toJson(equipmentInfoModel));
        this.equipmentInfoModel = equipmentInfoModel;
        String Eid = equipmentInfoModel.getId();
        String Id = equipmentInfoModel.getPid();
        String Lid = equipmentInfoModel.getLid();
        pid = Id;//植物id
        mPresenter.getAutoPara(Eid,Id,Lid);//plant_info
    }

    @Override
    public void getAutoParaSuccess(List<AutoModel.ParaBean> paraBeans) {
        LogUtil.d(TAG,"(restore 2 :plant_info success)getAutoParaSuccess:"+ new Gson().toJson(paraBeans));
        //Auto
        AutoModel autoModel = new AutoModel();
        autoModel.setCMD("Auto");
        autoModel.setUUID(oldEquipmentInfo.getUuid());
        autoModel.setPid(pid);

        int plantInfoSize = paraBeans.size();
        if(plantInfoSize > 0){
            autoModel.setPara(paraBeans);
        }
        String autoCmd = new Gson().toJson(autoModel);
        LogUtil.d(TAG,"发送auto命令的参数："+autoCmd);

        AutoParaModel model = new AutoParaModel(ConstUtil.CMD_DEVICE_INTELLIGENT_CONTROL, Integer.parseInt(equipmentInfoModel.getPid()),
                Integer.parseInt(oldEquipmentInfo.getUuid()), paraBeans);
        String request = gson.toJson(model);
        LogUtil.d(TAG,"getAutoParaSuccess(send auto ltid and uuid):"+oldEquipmentInfo.getLtid()+",uuid:"+oldEquipmentInfo.getUuid());
        MyLongToothService.sendDataFrame(this,ConstUtil.OP_AUTO,oldEquipmentInfo.getLtid(),request);
        MyLongToothService.setMyLongToothListener(this);
        //noDisturb
        //lightOn
    }

    @Override
    public void getAutoParaFailed() {
        LogUtil.d(TAG,"getAutoParaFailed");
    }

    @Override
    public void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans) {
        LogUtil.d(TAG, "获取首页所有的设备列表equipment_list:" + new Gson().toJson(listBeans));
        this.frontPageDeviceList = listBeans;
    }

    @Override
    protected void initBefore() {
        super.initBefore();
    }

    @Override
    protected AddEquipmentsContract.Presenter initPresenter() {
        return new AddEquipmentsPresenter(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText(Application.getStringText(R.string.connect_equipment));
        mSave.setVisibility(View.VISIBLE);
        mSave.setText(Application.getStringText(R.string.save));
        mRecyler.setLayoutManager(new LinearLayoutManager(this));
        mRecyler.setAdapter(mAdapter = new RecyclerAdapter<EquipmentModel>() {
            @Override
            protected int getItemViewType(int position, EquipmentModel equipmentModel) {
                return R.layout.item_add_equipments;
            }

            @Override
            protected ViewHolder<EquipmentModel> onCreateViewHolder(View root, int viewType) {
                return new MyViewHolder(root);
            }
        });
        mEmptyView.bind(mRecyler);
        mEmptyView.setEmptyImg(R.mipmap.img_equipment_empty);
        mEmptyView.setEmptyText(R.string.equipment_empty);
        setPlaceHolderView(mEmptyView);

        if (!LongToothUtil.isInitSuccess) {
            LongToothUtil.longToothInit();
        }
    }
    @Override
    protected void initData() {
        super.initData();
        mToken = Account.getToken();
        //fun add 获取所有的设备列表,和mdns出来的设备进行比较，提示用户重新绑定和恢复配置
        mPresenter.getAllEquipments(mToken, "0", "0");

        mLoadingAddEquipments.setForegroundColor(Color.parseColor("#87BC52"));
        mLoadingAddEquipments.setVisibility(View.VISIBLE);
        mLoadingAddEquipments.start();
        EasyLinkParams easylinkPara = new EasyLinkParams();
        easylinkPara.ssid = ssid;
        easylinkPara.password = password;
        easylinkPara.runSecond = 60000;
        easylinkPara.sleeptime = 20;
        elink.startEasyLink(easylinkPara, new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                LogUtil.d(TAG, "配网成功（EasyLink onSuccess）message:" + message + ",code:" + code);
            }

            @Override
            public void onFailure(int code, String message) {
                LogUtil.d(TAG, "配网失败（EasyLink onFailure）message:" + message + ",code:" + code);
                Application.showToast(message);
                finish();
            }
        });
        String serviceInfo = ConstUtil.MDNS_SERVICE_NAME;
        mdns.startSearchDevices(serviceInfo, new SearchDeviceCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                super.onSuccess(code, message);
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                Application.showToast(message);
                finish();
            }

            @Override
            public void onDevicesFind(int code, JSONArray deviceStatus) {
                super.onDevicesFind(code, deviceStatus);
                String content = deviceStatus.toString();
                LogUtil.d(TAG, "MDNS onDevicesFind: " + Thread.currentThread().getName() + "===>" + content);
                if (!TextUtils.isEmpty(content) && !content.equals("[]")) {
                    List<EquipmentModel> scanList = gson.fromJson(content, new TypeToken<List<EquipmentModel>>() {
                    }.getType());
                    for (final EquipmentModel equipmentModel : scanList) {
                        String deviceName = equipmentModel.getDEVNAME();
                        String deviceBoundState = equipmentModel.get_$BOUNDSTATUS310();
                        String deviceSeries = plantSeriesCard.getSeries();
                        //搜索所有搜索到该系列的设备，如果还没有添加过，并且设备没有被绑定过则显示
                        //养花机系列的名称和对应mdns出来的名字要一致才显示出来
                        if(!TextUtils.isEmpty(deviceName) && !TextUtils.isEmpty(deviceBoundState) && !TextUtils.isEmpty(deviceSeries)){
                            if (!series.contains(deviceName) && deviceName.startsWith(deviceSeries)
                                    && deviceBoundState.equals("notBound")) {
                                LogUtil.d(TAG,"满足条件，加入列表");
                                series.add(deviceName);
                                Run.onUiAsync(new Action() {
                                    @Override
                                    public void call() {
                                        LogUtil.d(TAG,"满足条件，显示在界面上");
                                        mAdapter.add(equipmentModel);
                                        mEmptyView.triggerOk();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        LogUtil.d(TAG, "60s定时到了======================================停止搜索设备");
                        mLoadingAddEquipments.stop();
                        mLoadingAddEquipments.setVisibility(View.INVISIBLE);
                        tv_searchText.setVisibility(View.INVISIBLE);
                        mdns.stopSearchDevices(null);
                        elink.stopEasyLink(null);
                        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
                    }
                });
            }
        };
        timer.schedule(task, 60000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG,"onDestroy=======================");
        mdns.stopSearchDevices(null);
        elink.stopEasyLink(null);
        clearData();
    }

    private void clearData() {
        isAddSuccess = false;
        isRestore = true;

        if(rbTimer != null){
            LogUtil.d(TAG,"onDestory rbTimer关闭");
            rbTimer.cancel();
            rbTimer = null;
        }

        if(restoreTime != null){
            LogUtil.d(TAG,"onDestory restoreTimer cancel=============================================");
            restoreTime.cancel();
            restoreTime = null;
        }

        if(addTimer != null){
            LogUtil.d(TAG,"onDestory addTimer cancel ================================================");
            addTimer.cancel();
            addTimer = null;
        }
        if(successLtidList != null && successLtidList.size() > 0){
            successLtidList.clear();
            successLtidList = null;
        }

        if(newDeviceList != null && newDeviceSize > 0){
            newDeviceSize = 0;
            newDeviceList.clear();
            newDeviceList = null;
        }

        if(oldEquipmentInfoList != null && oldDeviceSize > 0){
            oldDeviceSize = 0;
            oldEquipmentInfoList.clear();
            oldEquipmentInfoList = null;
        }

        if(bindOldDeviceFailList != null && bindOldDeviceFailList.size() > 0){
            bindOldDeviceFailList.clear();
            bindOldDeviceFailList = null;
        }

        if(bindNewDeviceFailList != null && bindNewDeviceFailList.size() > 0){
            bindNewDeviceFailList.clear();
            bindNewDeviceFailList = null;
        }

        if(ndOldDeviceSuccessList != null && ndOldDeviceSuccessList.size() > 0){
            ndOldDeviceSuccessList.clear();
            ndOldDeviceSuccessList = null;
        }

        if(ndOldDeviceFailList != null && ndOldDeviceFailList.size() > 0){
            ndOldDeviceFailList.clear();
            ndOldDeviceFailList = null;
        }

        if(lightOnOldDeviceSuccessList != null && lightOnOldDeviceSuccessList.size() > 0){
            lightOnOldDeviceSuccessList.clear();
            lightOnOldDeviceSuccessList = null;
        }

        if(lightOldDeviceFailList != null && lightOldDeviceFailList.size() > 0){
            lightOldDeviceFailList.clear();
            lightOldDeviceFailList = null;
        }

        if(autoOldDeviceSuccessList != null && autoOldDeviceSuccessList.size() > 0){
            autoOldDeviceSuccessList.clear();
            autoOldDeviceSuccessList = null;
        }

        if(autoOldDeviceFailList != null && autoOldDeviceFailList.size() > 0){
            autoOldDeviceFailList.clear();
            autoOldDeviceFailList = null;
        }

        if(opStateNewList != null && opStateNewList.size() > 0){
            opStateNewList.clear();
            opStateNewList = null;
        }

        if(opStateOldList != null && opStateOldList.size() > 0){
            opStateOldList.clear();
            opStateOldList = null;
        }
    }

    class MyViewHolder extends RecyclerAdapter.ViewHolder<EquipmentModel> implements AddEquipmentsRecylerContract.View {
        @BindView(R.id.equipment_photo)
        ImageView mPhoto;

        @BindView(R.id.tv_equipment_name)
        TextView mName;

        @BindView(R.id.tv_mac)
        TextView mMac;

        @BindView(R.id.cb_add)
        CheckBox mCbAdd;

        @BindView(R.id.img_add_failed)
        ImageView mAddFailed;

        @BindView(R.id.img_add_success)
        ImageView mAddSuccess;

        private AddEquipmentsRecylerContract.Presenter mPresenter;

        public MyViewHolder(View itemView) {
            super(itemView);
            EventBus.getDefault().register(this);
            new AddEquipmentsRecylerPresenter(this);
        }
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onEidtPersonDataSuccess(MessageEvent messageEvent) {
            if (messageEvent.getMessage().equals(mData.getLTID()) &&
                    messageEvent.getType().equals(MessageEvent.FAILED)) {
                mCbAdd.setChecked(true);
                mCbAdd.setVisibility(View.GONE);
//                equipmentModels.remove(mData);
                mAddFailed.setVisibility(View.VISIBLE);
                mAddSuccess.setVisibility(View.GONE);
            } else if (messageEvent.getMessage().equals(mData.getLTID()) &&
                    messageEvent.getType().equals(MessageEvent.SUCCESS)) {
                mCbAdd.setVisibility(View.GONE);
                mAddFailed.setVisibility(View.GONE);
                mAddSuccess.setVisibility(View.VISIBLE);
                equipmentModels.remove(mData);
            }
        }

        @Override
        protected void onBind(EquipmentModel equipmentModel) {
            mName.setText(equipmentModel.getDEVNAME());
            mMac.setText(equipmentModel.getMAC());
            String type = equipmentModel.getDEVNAME().substring(0, 5);
            mPresenter.getEquipmentPhoto("2", type);
        }

        @OnClick(R.id.cb_add)
        void onAddClick() {
            if (mCbAdd.isChecked()) {
                equipmentModels.add(mData);
            } else {
                equipmentModels.remove(mData);
            }
            LogUtil.d(TAG,"选中的设备："+new Gson().toJson(equipmentModels));
        }

        @Override
        public void showError(int str) {

        }

        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }

        @Override
        public void onConnectionConflict() {
            UiTool.onConnectionConflict(AddEquipmentsActivity.this);
        }

        @Override
        public void setPresenter(AddEquipmentsRecylerContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void getEquipmentPhotoSuccess(EquipmentPhotoModel photoModel) {
            String photoPath = photoModel == null ? "" : photoModel.getPhoto();
            GlideApp.with(AddEquipmentsActivity.this)
                    .load(photoPath)
                    .centerCrop()
                    .placeholder(R.mipmap.img_content_empty)
                    .into(mPhoto);
        }
    }
}
