package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class SecondSettingActivity extends Activity {

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, SecondSettingActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_second_setting;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("设置");
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_equipment_setting)
    void onEquipmentSettingClick() {
        EquipmentSettingActivity.show(this);
    }

    @OnClick(R.id.tv_plant_setting)
    void onPlantSettingClick() {
        PlantSettingActivity.show(this);
    }

    @OnClick(R.id.tv_equipment_info)
    void onEquipmentInfoClick() {
        EquipmentInfoActivity.show(this);
    }

}
