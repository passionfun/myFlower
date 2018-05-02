package bocai.com.yanghuajien.ui.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.RecyclerAdapter;
import bocai.com.yanghuajien.base.common.Common;
import bocai.com.yanghuajien.model.EquipmentRspModel;
import bocai.com.yanghuajien.model.GroupRspModel;
import bocai.com.yanghuajien.ui.intelligentPlanting.PlantingDateAct;
import bocai.com.yanghuajien.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/28.
 * 邮箱 yuanfei221@126.com
 */

public class GroupViewHolder extends RecyclerAdapter.ViewHolder<GroupRspModel.ListBean> {
    @BindView(R.id.checkbox)
    CheckBox mCheckbox;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.view_divider)
    View divider;

    private RecyclerAdapter<GroupRspModel.ListBean.EquipmentBean> mAdapter;

    public GroupViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(GroupRspModel.ListBean listBean) {
        mCheckbox.setText(listBean.getGroupName());
        mRecycler.setLayoutManager(new LinearLayoutManager(mRecycler.getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<GroupRspModel.ListBean.EquipmentBean>() {
            @Override
            protected int getItemViewType(int position, GroupRspModel.ListBean.EquipmentBean equipmentBean) {
                return R.layout.item_equipment;
            }

            @Override
            protected ViewHolder<GroupRspModel.ListBean.EquipmentBean> onCreateViewHolder(View root, int viewType) {
                return new MyViewHolder(root);
            }
        });
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<GroupRspModel.ListBean.EquipmentBean>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, GroupRspModel.ListBean.EquipmentBean equipmentBean) {
                super.onItemClick(holder, equipmentBean);
                List<EquipmentRspModel.ListBean> listBeans = Account.getListBeans();
                for (EquipmentRspModel.ListBean bean : listBeans) {
                    if (bean.getEquipName().equals(equipmentBean.getEquipName())){
                        String url = Common.Constance.H5_BASE + "product.html?id=" + bean.getId();
                            PlantingDateAct.show(mRecycler.getContext(), url, bean);
                    }
                }
            }
        });
        mAdapter.replace(listBean.getEquipment());
    }


    @OnClick(R.id.checkbox)
    void onCheckboxClick() {
        if (mCheckbox.isChecked()) {
            mRecycler.setVisibility(View.GONE);
            divider.setVisibility(View.VISIBLE);
        } else {
            mRecycler.setVisibility(View.VISIBLE);
            if (mAdapter.getItemCount()>0){
                divider.setVisibility(View.GONE);
            }else {
                divider.setVisibility(View.VISIBLE);
            }
        }
    }


    class MyViewHolder extends RecyclerAdapter.ViewHolder<GroupRspModel.ListBean.EquipmentBean> {

        @BindView(R.id.tv_equipment_name)
        TextView mName;


        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupRspModel.ListBean.EquipmentBean equipmentBean) {
            mName.setText(equipmentBean.getEquipName());
        }
    }
}
