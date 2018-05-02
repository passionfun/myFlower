package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.RecyclerAdapter;
import bocai.com.yanghuajien.model.LifeCycleModel;
import bocai.com.yanghuajien.util.ActivityUtil;
import bocai.com.yanghuajien.util.UiTool;
import butterknife.BindView;

/**
 * Created by apple on 17-11-27.
 */

public class ChoosePop extends PopupWindow {
    private Context mContext;
    private RecyclerView mRecycler;
    private RecyclerAdapter<LifeCycleModel.ListBean> mAdapter;
    private ChoosePop.SelectListener mlistener;
    private ImageView mClose;

    public ChoosePop(final Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_equipment_list, null);
        this.setWidth(UiTool.dip2px(mContext, 240));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);

        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                ActivityUtil.setBackgroundAlpha((Activity) context, 1f);
            }
        });
        mRecycler = view.findViewById(R.id.recycler);
        mClose = view.findViewById(R.id.img_close);
        mClose.setVisibility(View.VISIBLE);
        init();
    }

    private void init() {
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter=new RecyclerAdapter<LifeCycleModel.ListBean>() {
            @Override
            protected int getItemViewType(int position, LifeCycleModel.ListBean listBean) {
                return R.layout.item_equipment_list;
            }

            @Override
            protected ViewHolder<LifeCycleModel.ListBean> onCreateViewHolder(View root, int viewType) {
                return new ChoosePop.ViewHolder(root);
            }
        };
        mRecycler.setAdapter(mAdapter);

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<LifeCycleModel.ListBean>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, LifeCycleModel.ListBean listBean) {
                mlistener.selected(listBean);
                ChoosePop.this.dismiss();
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePop.this.dismiss();
            }
        });
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<LifeCycleModel.ListBean> {
        @BindView(R.id.tv_equipment_name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(LifeCycleModel.ListBean listBean) {
            name.setText(listBean.getTitle());
        }
    }

    public void addData(List<LifeCycleModel.ListBean> list) {
        mAdapter.add(list);
    }

    public void setOnSelectListener(ChoosePop.SelectListener listener) {
        mlistener = listener;
    }

    public static interface SelectListener {
        void selected(LifeCycleModel.ListBean listBean);
    }
}
