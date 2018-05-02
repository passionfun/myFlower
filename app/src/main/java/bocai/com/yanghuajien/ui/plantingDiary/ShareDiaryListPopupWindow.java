package bocai.com.yanghuajien.ui.plantingDiary;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.util.ActivityUtil;

/**
 * 作者 yuanfei on 2017/11/13.
 * 邮箱 yuanfei221@126.com
 */

public class ShareDiaryListPopupWindow extends PopupWindow {
    private ImageView mCopyLink,mShareQq,mShareWechat,mShareFriends;
    private TextView mCancel;
    private ItemClickListener mListener;



    public ShareDiaryListPopupWindow(final Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_list_share, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        mCancel = view.findViewById(R.id.tv_cancel);
        mCopyLink = view.findViewById(R.id.img_copy_link);
        mShareQq = view.findViewById(R.id.img_share_qq);
        mShareWechat = view.findViewById(R.id.img_share_wechat);
        mShareFriends = view.findViewById(R.id.img_share_friends);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });

        mCopyLink.setOnClickListener(new View.OnClickListener() {
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

    public void setOnTtemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    interface ItemClickListener {
        void onItemClick(View view);
    }
}
