package bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.ui.intelligentPlanting.SecondSettingActivity;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class HorizntalAdapter extends RecyclerView.Adapter<HorizntalAdapter.ViewHolder> {

    private List<Integer> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    public  int mPosition;

    public HorizntalAdapter(List<Integer> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_horizontal, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mPosition = position;
        mListener.currentPosition();
        mCardAdapterHelper.onBindViewHolder(holder.mFramelayout, position, mList.size());
//        holder.mName.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int getPosition() {
        return mPosition;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private FrameLayout mFramelayout;
        private ImageView mSetting;

        public ViewHolder(final View itemView) {
            super(itemView);
            mFramelayout = itemView.findViewById(R.id.frame_container);
            mName = itemView.findViewById(R.id.tv_equipment_name);
            mSetting = itemView.findViewById(R.id.img_setting);
            mSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SecondSettingActivity.show(view.getContext());
                }
            });
        }

    }

    private CurrentPositionListener mListener;
    public void setOnCurrentPositionListener( CurrentPositionListener listener){
        mListener = listener;
    }
    public interface CurrentPositionListener{
        void currentPosition();
    }

}
