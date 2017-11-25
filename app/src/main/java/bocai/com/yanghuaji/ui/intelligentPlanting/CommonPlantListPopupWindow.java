package bocai.com.yanghuaji.ui.intelligentPlanting;

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

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.model.PlantRspModel;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.UiTool;
import butterknife.BindView;

/**
 * 作者 yuanfei on 2017/11/23.
 * 邮箱 yuanfei221@126.com
 */

public class CommonPlantListPopupWindow extends PopupWindow {
    private Context mContext;
    private RecyclerView mRecycler;
    private RecyclerAdapter<PlantRspModel.PlantCard> mAdapter;
    private SelectListener mlistener;
    private ImageView mClose;

    public CommonPlantListPopupWindow(final Context context) {
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
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<PlantRspModel.PlantCard>() {

            @Override
            protected int getItemViewType(int position, PlantRspModel.PlantCard equipmentCard) {
                return R.layout.item_equipment_list;
            }

            @Override
            protected ViewHolder onCreateViewHolder(View root, int viewType) {
                return new CommonPlantListPopupWindow.ViewHolder(root);
            }
        });

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<PlantRspModel.PlantCard>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, PlantRspModel.PlantCard equipmentCard) {
                mlistener.selected(equipmentCard);
                CommonPlantListPopupWindow.this.dismiss();
            }

        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonPlantListPopupWindow.this.dismiss();
            }
        });
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<PlantRspModel.PlantCard> {
        @BindView(R.id.tv_equipment_name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(PlantRspModel.PlantCard equipmentCard) {
            name.setText(equipmentCard.getPlantName());
        }
    }

    public void addData(List<PlantRspModel.PlantCard> equipmentCards) {
        mAdapter.add(equipmentCards);
    }

    public void setOnSelectListener(SelectListener listener) {
        mlistener = listener;
    }

    public static interface SelectListener {
        void selected(PlantRspModel.PlantCard card);
    }

}
