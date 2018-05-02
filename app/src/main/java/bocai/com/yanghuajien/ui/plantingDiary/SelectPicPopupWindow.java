package bocai.com.yanghuajien.ui.plantingDiary;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.util.ActivityUtil;

/**
 * 作者 yuanfei on 2017/11/13.
 * 邮箱 yuanfei221@126.com
 */

public class SelectPicPopupWindow extends PopupWindow {
    private ItemClickListener mListener;
    private TextView mTakePhoto, mFromGallery, mCancel;

    public void setOnTtemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    public SelectPicPopupWindow(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_add_picture, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        mTakePhoto = view.findViewById(R.id.tv_take_photo);
        mFromGallery = view.findViewById(R.id.tv_from_gallery);
        mCancel = view.findViewById(R.id.tv_cancel);
        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        mFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ActivityUtil.setBackgroundAlpha((Activity) context, 1f);
            }
        });
    }

    public interface ItemClickListener {
        void onItemClick(View view);
    }
}
