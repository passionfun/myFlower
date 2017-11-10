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

public class ModifyPasswordActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, ModifyPasswordActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitle.setText("修改密码");
    }

    @OnClick(R.id.img_back)
    void onBackClick(){
        finish();
    }

    // todo 更改密码提价
    @OnClick(R.id.bt_save)
    void onSaveSubmit() {

    }
}
