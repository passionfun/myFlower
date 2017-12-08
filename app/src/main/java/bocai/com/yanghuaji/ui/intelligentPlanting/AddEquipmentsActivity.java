package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.EquipmentModel;
import bocai.com.yanghuaji.model.EquipmentPhotoModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquipmentsRecylerContract;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import io.fog.fog2sdk.MiCODevice;
import io.fogcloud.fog_mdns.helper.SearchDeviceCallBack;

/**
 * 作者 yuanfei on 2017/12/8.
 * 邮箱 yuanfei221@126.com
 */

public class AddEquipmentsActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.recycler)
    RecyclerView mRecyler;

    private RecyclerAdapter<EquipmentModel> mAdapter;
    private Gson gson = new Gson();
    //搜索设备用
    private MiCODevice micodev;
    //所有在线设备的系列名称集合
    List<String> series = new ArrayList<>();
    //所有已经添加过的设备
    private List<EquipmentRspModel.ListBean> listBeans = Account.getListBeans();

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, AddEquipmentsActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_equipments;
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("连接设备");
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
    }


    @Override
    protected void initData() {
        super.initData();
        //开始搜索设备
        final String serviceName = "_easylink._tcp.local.";
        micodev = new MiCODevice(this);
        micodev.startSearchDevices(serviceName, new SearchDeviceCallBack() {
            @Override
            public void onDevicesFind(int code, JSONArray deviceStatus) {
                super.onDevicesFind(code, deviceStatus);
                String content = deviceStatus.toString();
                Log.d("shc", "onDevicesFind: " + content);
                if (!TextUtils.isEmpty(content) && !content.equals("[]")) {
                    String jsonContent = content;
                    micodev.stopSearchDevices(null);
                    List<EquipmentModel> equipmentModels = gson.fromJson(jsonContent, new TypeToken<List<EquipmentModel>>() {
                    }.getType());
                    for (EquipmentModel equipmentModel : equipmentModels) {
                        String seriesName = equipmentModel.getDEVNAME();
                        //搜索所有搜索到的设备，如果还没有添加过，则显示
                        if (!series.contains(seriesName)&&!isAdded(equipmentModel.getLTID())) {
                            series.add(seriesName);
                            mAdapter.add(equipmentModel);
                        }
                    }
                }
            }
        });
    }


    private boolean isAdded(String longtoothId){
        List<String> longtoothS = new ArrayList<>();
        if (listBeans!=null&&listBeans.size()>0){
            for (EquipmentRspModel.ListBean listBean : listBeans) {
                longtoothS.add(listBean.getLTID());
            }
            if (longtoothS.contains(longtoothId)){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }

    }


    class MyViewHolder extends RecyclerAdapter.ViewHolder<EquipmentModel> implements AddEquipmentsRecylerContract.View{
        @BindView(R.id.equipment_photo)
        ImageView mPhoto;

        @BindView(R.id.tv_equipment_name)
        TextView mName;

        @BindView(R.id.tv_mac)
        TextView mMac;

        @BindView(R.id.cb_add)
        CheckBox mCbAdd;





        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(EquipmentModel equipmentModel) {
            mName.setText(equipmentModel.getDEVNAME());
            mMac.setText(equipmentModel.getMAC());
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
        public void setPresenter(AddEquipmentsRecylerContract.Presenter presenter) {

        }

        @Override
        public void getEquipmentPhotoSuccess(EquipmentPhotoModel photoModel) {

        }

        @Override
        public void addEquipmentsSuccess(List<EquipmentCard> equipmentCards) {

        }
    }




}
