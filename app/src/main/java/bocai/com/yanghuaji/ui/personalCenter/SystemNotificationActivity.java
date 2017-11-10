package bocai.com.yanghuaji.ui.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 系统通知
 * 作者 yuanfei on 2017/11/10.
 * 邮箱 yuanfei221@126.com
 */

public class SystemNotificationActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, SystemNotificationActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_system_notification;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitle.setText("系统通知");
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }


}
