package bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bocai.com.yanghuaji.R;

/**
 * 作者 yuanfei on 2017/11/20.
 * 邮箱 yuanfei221@126.com
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {
    private int currentPos = 0;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_horizontal, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mListener.setPos(position);
        currentPos = position;
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public int getCurrentPos() {
        return currentPos;
    }


    private SetPosListener mListener;
    public  void setOnSetPosListener(SetPosListener listener){
        mListener = listener;
    }
    public interface SetPosListener{
        void setPos(int pos);
    }
}
