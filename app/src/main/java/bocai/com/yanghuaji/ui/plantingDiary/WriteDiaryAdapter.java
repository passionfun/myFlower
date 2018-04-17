package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import boc.com.imgselector.GlideApp;
import bocai.com.yanghuaji.R;

/**
 * Created by shc on 2018/4/17.
 */

public class WriteDiaryAdapter extends RecyclerView.Adapter<WriteDiaryAdapter.MyViewHolder> {

    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List<String> list = new ArrayList<>();
    private Context context;
    private onAddPicClickListener mOnAddPicClickListener;

    public WriteDiaryAdapter(Context context,onAddPicClickListener mOnAddPicClickListener) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    @Override
    public WriteDiaryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_diary_photos,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WriteDiaryAdapter.MyViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_CAMERA) {
            GlideApp.with(holder.imagePhoto.getContext())
                    .load(list.get(position))
                    .centerCrop()
                    .into(holder.imagePhoto);
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else {
            GlideApp.with(holder.imagePhoto.getContext())
                    .load(R.mipmap.img_add_photos)
                    .centerCrop()
                    .into(holder.imagePhoto);
            holder.imgDelete.setVisibility(View.GONE);
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(position);
                    WriteDiaryAdapter.this.notifyDataSetChanged();
                }
            });
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    @Override
    public int getItemCount() {
        if (list.size() < 9) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imagePhoto;
        ImageView imgDelete;
        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }


    public interface onAddPicClickListener {
        void onAddPicClick();
    }
}
