package bocai.com.yanghuajien.ui.personalCenter;

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
 * 作者 yuanfei on 2017/11/17.
 * 邮箱 yuanfei221@126.com
 */

public class SexSelectPopupWindow extends PopupWindow {
    private ItemClickListener mListener;
    private TextView mMan, mWomac,mSecret;

    public void setOnTtemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    public SexSelectPopupWindow(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_sex_select, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        mMan = view.findViewById(R.id.tv_take_photo);
        mWomac = view.findViewById(R.id.tv_from_gallery);
        mSecret = view.findViewById(R.id.tv_secret);
        mMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        mWomac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        mSecret.setOnClickListener(new View.OnClickListener() {
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

    public void setTextTop(String content) {
        mMan.setText(content);
    }

    public void setTextBottom(String content) {
        mWomac.setText(content);
    }

    interface ItemClickListener {
        void onItemClick(View view);
    }

}
