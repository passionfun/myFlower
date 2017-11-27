package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.GroupListContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.GroupListPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public class GroupListPopupWindow extends PopupWindow implements GroupListContract.View {
    private Context mContext;
    private RecyclerView mRecycler;
    private ImageView mAdd;
    private RecyclerAdapter<GroupRspModel.ListBean> mAdapter;
    private SelectListener mListener;
    private GroupListContract.Presenter mPresenter;
    private ProgressDialog dialog;

    public GroupListPopupWindow(final Context context) {
        mContext = context;
        mPresenter = new GroupListPresenter(this);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_group_list, null);
        this.setWidth(UiTool.dip2px(mContext, 240));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ActivityUtil.setBackgroundAlpha((Activity) context, 1f);
            }
        });
        mRecycler = view.findViewById(R.id.recycler);
        mAdd = view.findViewById(R.id.img_add_group);
        init();
    }

    private void init() {
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<GroupRspModel.ListBean>() {

            @Override
            protected int getItemViewType(int position, GroupRspModel.ListBean listBean) {
                return R.layout.item_equipment_list;
            }

            @Override
            protected ViewHolder<GroupRspModel.ListBean> onCreateViewHolder(View root, int viewType) {
                return new GroupListPopupWindow.ViewHolder(root);
            }
        });

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<GroupRspModel.ListBean>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, GroupRspModel.ListBean groupCard) {
                mListener.selected(groupCard);
                GroupListPopupWindow.this.dismiss();
            }

        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  添加分组
                final EditText editText = new EditText(view.getContext());
                editText.setHint("请输入组名：");
                AlertDialog.Builder addGroupDialog = new AlertDialog.Builder(view.getContext());
                addGroupDialog.setTitle("添加分组").setView(editText);
                addGroupDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.addGroup(Account.getToken(), editText.getText().toString());

                    }
                });

                addGroupDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GroupListPopupWindow.this.dismiss();
                    }
                });
                addGroupDialog.show();
            }
        });
    }

    @Override
    public void showError(int str) {
        hideLoading();
        Application.showToast(str);
    }

    @Override
    public void showLoading() {
        dialog = new ProgressDialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void setPresenter(GroupListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void addGroupSuccess(GroupRspModel.ListBean groupCard) {
        mListener.selected(groupCard);
        this.dismiss();
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupRspModel.ListBean> {
        @BindView(R.id.tv_equipment_name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupRspModel.ListBean groupCard) {
            name.setText(groupCard.getGroupName());
        }
    }

    public void addData(List<GroupRspModel.ListBean> groupCards) {
        mAdapter.add(groupCards);
    }

    public void setOnSelectListener(SelectListener listener) {
        mListener = listener;
    }

    public  interface SelectListener {
        void selected(GroupRspModel.ListBean groupCard);
    }

}
