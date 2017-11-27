package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.EquipmentsByGroupModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EditGroupContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EditGroupPresenter;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public class EditGroupActivity extends PresenterActivity<EditGroupContract.Presenter>
        implements EditGroupContract.View {

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_right)
    TextView mSave;

    @BindView(R.id.et_group_name)
    EditText mEtName;

    @BindView(R.id.tv_content)
    TextView mContent;

    public static final String KEY_GROUP_ID = "KEY_GROUP_ID";
    public static final String KEY_GROUP_NAME = "KEY_GROUP_NAME";
    private RecyclerAdapter<EquipmentCard> mAdapter;
    private String mGroupId;
    private String mGroupName;
    private List<String> deleteIds = new ArrayList<>();
    private String ids;

    //显示的入口
    public static void show(Context context, String groupId, String groupName) {
        Intent intent = new Intent(context, EditGroupActivity.class);
        intent.putExtra(KEY_GROUP_ID, groupId);
        intent.putExtra(KEY_GROUP_NAME, groupName);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_edit_group;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mGroupId = bundle.getString(KEY_GROUP_ID);
        mGroupName = bundle.getString(KEY_GROUP_NAME);
        return super.initArgs(bundle);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_right)
    void onSaveClick() {
        //TODO 保存
        String token = Account.getToken();
        mGroupName = mEtName.getText().toString();
        StringBuffer buffer = new StringBuffer();
        for (String deleteId : deleteIds) {
            buffer.append(deleteId).append(",");
        }
        ids = buffer.toString();
        mPresenter.editGroup(mGroupId, token, mGroupName, ids);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("编辑分组");
        mSave.setVisibility(View.VISIBLE);
        mSave.setText("保存");
        mEtName.setText(mGroupName);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<EquipmentCard>() {
            @Override
            protected int getItemViewType(int position, EquipmentCard card) {
                return R.layout.item_equipment_list_group_manager;
            }

            @Override
            protected ViewHolder<EquipmentCard> onCreateViewHolder(View root, int viewType) {
                return new MyViewHolder(root);
            }
        });
        mPresenter.getEquipmentsByGroup(Account.getToken(), mGroupId);
    }

    @Override
    public void getEquipmentsByGroupSuccess(EquipmentsByGroupModel model) {
        mAdapter.replace(model.getList());
        mContent.setText("已添加" + mAdapter.getItemCount() + "个设备");
        mEtName.setText(model.getGroupName());
    }

    @Override
    public void editGroupSuccess(EquipmentsByGroupModel model) {
        mAdapter.replace(model.getList());
        mContent.setText("已添加" + mAdapter.getItemCount() + "个设备");
        mEtName.setText(model.getGroupName());
    }

    @Override
    protected EditGroupContract.Presenter initPresenter() {
        return new EditGroupPresenter(this);
    }


    class MyViewHolder extends RecyclerAdapter.ViewHolder<EquipmentCard> {
        @BindView(R.id.tv_name)
        TextView mName;

        @BindView(R.id.img_delete)
        ImageView mImage;

        private String mDeleteId;
        private boolean isDelete = false;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(EquipmentCard card) {
            mName.setText(card.getEquipName());
            mDeleteId = card.getId();
        }

        @OnClick(R.id.img_delete)
        void onDeleteClick() {
            //删除设备操作
            isDelete = !isDelete;
            if (isDelete) {
                deleteIds.add(mDeleteId);
                mImage.setImageResource(R.mipmap.img_edit_group);
            } else {
                deleteIds.remove(mDeleteId);
                mImage.setImageResource(R.mipmap.img_delete_group);
            }
        }
    }


}
