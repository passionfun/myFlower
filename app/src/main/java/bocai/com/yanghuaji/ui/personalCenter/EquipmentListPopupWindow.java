package bocai.com.yanghuaji.ui.personalCenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.UiTool;
import butterknife.BindView;

/**
 * 作者 yuanfei on 2017/11/23.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentListPopupWindow extends PopupWindow {
    private Context mContext;
    private RecyclerView mRecycler;
    private RecyclerAdapter<EquipmentCard> mAdapter;
    private SelectListener mlistener;

    public EquipmentListPopupWindow(final Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_equipment_list, null);
        this.setWidth(UiTool.dip2px(mContext,240));
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
        init();
    }

    private void init(){
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<EquipmentCard>() {

            @Override
            protected int getItemViewType(int position, EquipmentCard equipmentCard) {
                return R.layout.item_equipment_list;
            }

            @Override
            protected ViewHolder onCreateViewHolder(View root, int viewType) {
                return new EquipmentListPopupWindow.ViewHolder(root);
            }
        });

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<EquipmentCard>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, EquipmentCard equipmentCard) {
                mlistener.selected(equipmentCard);
                EquipmentListPopupWindow.this.dismiss();
            }

        });
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<EquipmentCard>{
        @BindView(R.id.tv_equipment_name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(EquipmentCard equipmentCard) {
            name.setText(equipmentCard.getEquipName());
        }
    }

    public void addData(List<EquipmentCard> equipmentCards){
        mAdapter.add(equipmentCards);
    }

    public void setOnSelectListener(SelectListener listener){
        mlistener = listener;
    }

    public  interface SelectListener{
        void selected(EquipmentCard card);
    }

}
