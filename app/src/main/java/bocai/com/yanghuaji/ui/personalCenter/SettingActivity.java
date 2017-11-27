package bocai.com.yanghuaji.ui.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/10.
 * 邮箱 yuanfei221@126.com
 */

public class SettingActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitle.setText("设置");
    }


    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    //新手指南
    @OnClick(R.id.tv_new_guide)
    void onGuideClick() {
        GuideActivity.show(this,1);
    }

    @OnClick(R.id.tv_service_term)
    void serviceTerm(){
        GuideActivity.show(this,2);
    }

}
