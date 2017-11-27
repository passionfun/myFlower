package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.util.ActivityUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class AddEquipmentDisplayActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.scroll_root)
    ScrollView mRoot;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, AddEquipmentDisplayActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_equipment_display;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("添加设备");
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_next)
    void onNextClick() {
        AddWifiActivity.show(this);

    }


    @OnClick(R.id.tv_show_pop)
    void onShowPopClick() {
        ShowStatePopupWindow popupWindow = new ShowStatePopupWindow(this);
        popupWindow.setOnTtemClickListener(new ShowStatePopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                // 确认点击时候触发
            }
        });

        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        popupWindow.showAtLocation(mRoot, Gravity.CENTER,0,0);
    }

}
