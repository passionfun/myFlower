package bocai.com.yanghuaji.ui.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 种植记录
 * 作者 yuanfei on 2017/11/10.
 * 邮箱 yuanfei221@126.com
 */

public class PlantingRecordActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.bt_planting)
    Button mBtPlanting;

    @BindView(R.id.bt_planted)
    Button mBtPlanted;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, PlantingRecordActivity.class));
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_planting_record;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitle.setText("种植记录");
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.bt_planting)
    void onPlantingClick() {
        mBtPlanting.setTextColor(Color.parseColor("#FFFFFF"));
        mBtPlanted.setTextColor(Color.parseColor("#999999"));
        mBtPlanting.setBackgroundResource(R.mipmap.img_plant_record_selected);
        mBtPlanted.setBackgroundResource(R.drawable.bt_plant_record_bg);
    }

    @OnClick(R.id.bt_planted)
    void onPlantedClick() {
        mBtPlanted.setTextColor(Color.parseColor("#FFFFFF"));
        mBtPlanting.setTextColor(Color.parseColor("#999999"));
        mBtPlanted.setBackgroundResource(R.mipmap.img_plant_record_selected);
        mBtPlanting.setBackgroundResource(R.drawable.bt_plant_record_bg);
    }






}
