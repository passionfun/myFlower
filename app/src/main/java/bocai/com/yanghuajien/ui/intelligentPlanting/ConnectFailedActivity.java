package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Activity;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.util.UiTool;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/22.
 * 邮箱 yuanfei221@126.com
 */

public class ConnectFailedActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;


    //显示的入口
    public static void show(Context context) {
        Intent intent = new Intent(context, ConnectFailedActivity.class);
        context.startActivity(intent);
    }

    protected int getContentLayoutId() {
        return R.layout.activity_connect_failed;
    }

    @Override
    protected void initData() {
        super.initData();
        UiTool.setBlod(mTitle);
        mTitle.setText(Application.getStringText(R.string.connect_equipment));
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_reload)
    void onReloadClick() {
//        AddEquipmentActivity.show(this);
        finish();
    }
}
