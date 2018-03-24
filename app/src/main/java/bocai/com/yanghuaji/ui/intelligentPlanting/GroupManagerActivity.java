package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.presenter.intelligentPlanting.GroupManagerContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.GroupManagerPresenter;
import bocai.com.yanghuaji.ui.main.MainActivity;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

import static bocai.com.yanghuaji.ui.main.MainActivity.GROUP_REFRESH;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public class GroupManagerActivity extends PresenterActivity<GroupManagerContract.Presenter>
        implements GroupManagerContract.View {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_right)
    TextView mAddGroup;

    private RecyclerAdapter<GroupRspModel.ListBean> mAdapter;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, GroupManagerActivity.class));
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_manager;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText(getResources().getString(R.string.group_manage));
        mAddGroup.setVisibility(View.VISIBLE);
        mAddGroup.setText(getResources().getString(R.string.add));
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<GroupRspModel.ListBean>() {
            @Override
            protected int getItemViewType(int position, GroupRspModel.ListBean listBean) {
                return R.layout.item_group_managerment;
            }

            @Override
            protected ViewHolder<GroupRspModel.ListBean> onCreateViewHolder(View root, int viewType) {
                return new MyViewHoler(root);
            }
        });
        mPresenter.getAllGroups(Account.getToken());
    }

    @OnClick(R.id.tv_right)
    void onAddGroupClick(){
        //  添加分组
        final EditText editText = new EditText(this);
        editText.setHint(getResources().getString(R.string.please_input_group_name));
        final AlertDialog.Builder addGroupDialog = new AlertDialog.Builder(this);
        addGroupDialog.setTitle(getResources().getString(R.string.add_group)).setView(editText);
        addGroupDialog.setPositiveButton(getResources().getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.addGroup(Account.getToken(), editText.getText().toString());

            }
        });

        addGroupDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        addGroupDialog.show();
    }

    @Override
    public void getAllGroupsSuccess(List<GroupRspModel.ListBean> groupCards) {
        mAdapter.replace(groupCards);
    }

    @Override
    public void deleteGroupSuccess() {
        //删除成功，刷新界面
        mPresenter.getAllGroups(Account.getToken());
        EventBus.getDefault().post(new MessageEvent(GROUP_REFRESH));
    }

    @Override
    public void addGroupSuccess(GroupRspModel.ListBean groupCard) {
        mPresenter.getAllGroups(Account.getToken());
        EventBus.getDefault().post(new MessageEvent(GROUP_REFRESH));
    }

    @Override
    protected GroupManagerContract.Presenter initPresenter() {
        return new GroupManagerPresenter(this);
    }


    class MyViewHoler extends RecyclerAdapter.ViewHolder<GroupRspModel.ListBean> {
        @BindView(R.id.img_delete)
        ImageView mDelete;

        @BindView(R.id.tv_edit)
        TextView mEdit;

        @BindView(R.id.tv_confirm)
        TextView mConfirm;

        @BindView(R.id.img_open_manager)
        ImageView mOpenManager;

        @BindView(R.id.tv_name)
        TextView mName;

        private String mGroupId;
        private  String mGroupName;

        public MyViewHoler(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupRspModel.ListBean listBean) {
            mGroupId = listBean.getId();
            mGroupName = listBean.getGroupName();
            mName.setText(mGroupName);

        }

        @OnClick(R.id.img_open_manager)
        void onOpenManagerClick() {
            //打开编辑
            mDelete.setVisibility(View.VISIBLE);
            mEdit.setVisibility(View.VISIBLE);
            mConfirm.setVisibility(View.VISIBLE);
            mOpenManager.setVisibility(View.GONE);
        }


        @OnClick(R.id.tv_confirm)
        void onConfirmClick() {
            //完成
            mDelete.setVisibility(View.GONE);
            mEdit.setVisibility(View.GONE);
            mConfirm.setVisibility(View.GONE);
            mOpenManager.setVisibility(View.VISIBLE);
        }


        @OnClick(R.id.tv_edit)
        void onEditClick() {
            //编辑操作
            EditGroupActivity.show(GroupManagerActivity.this,mGroupId,mGroupName);
            finish();
        }


        @OnClick(R.id.img_delete)
        void onDeleteClick() {
            //删除操作
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(mDelete.getContext());
            deleteDialog.setTitle(getResources().getString(R.string.ensure_delete_this_group));
            deleteDialog.setPositiveButton(getResources().getString(R.string.ensure), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mPresenter.deleteGroup(mGroupId);
                }
            });
            deleteDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            deleteDialog.show();
        }

    }

}
