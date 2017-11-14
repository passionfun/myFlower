package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.util.ActivityUtil;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class ShowStatePopupWindow extends PopupWindow {
    private ImageView mConfirm, mClose;
    private ItemClickListener mListener;

    public ShowStatePopupWindow(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_show_state, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        mConfirm = view.findViewById(R.id.img_confirm);
        mClose = view.findViewById(R.id.img_close);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowStatePopupWindow.this.dismiss();
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
