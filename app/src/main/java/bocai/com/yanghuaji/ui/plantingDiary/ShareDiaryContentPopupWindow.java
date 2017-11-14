package bocai.com.yanghuaji.ui.plantingDiary;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.util.ActivityUtil;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class ShareDiaryContentPopupWindow extends PopupWindow {
    private ImageView mSavePicture,mShareQq,mShareWechat,mShareFriends;
    private ImageView mDiaryCover,mCancelShare;
    private TextView mDiaryContent;
    private Context mContext;

    private ItemClickListener mListener;



    public ShareDiaryContentPopupWindow(final Context context){
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_diary_detial_share, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        mDiaryCover = view.findViewById(R.id.img_diary_cover);
        mCancelShare = view.findViewById(R.id.img_cancel_share);
        mDiaryContent = view.findViewById(R.id.tv_diary_content);
        mSavePicture = view.findViewById(R.id.img_save_picture);
        mShareQq = view.findViewById(R.id.img_share_qq);
        mShareWechat = view.findViewById(R.id.img_share_wechat);
        mShareFriends = view.findViewById(R.id.img_share_friends);
        mSavePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        mShareQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        mShareWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        mShareFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        mCancelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDiaryContentPopupWindow.this.dismiss();
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

    public void initData(String imagePath,String content){
        Glide.with(mContext)
                .load(imagePath)
                .into(mDiaryCover);
        mDiaryContent.setText(content);
    }


    public void setOnTtemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    interface ItemClickListener {
        void onItemClick(View view);
    }
}
