package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class AddEquipmentActivity extends Activity {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, AddEquipmentActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_equipment;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_scan)
    void onScanClick() {
        AddEquipmentDisplayActivity.show(this);
    }


}
