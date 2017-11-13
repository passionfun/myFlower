package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/13.
 * 邮箱 yuanfei221@126.com
 */

public class DiaryContentActivity extends Activity {
    @BindView(R.id.img_diary_cover)
    ImageView mDiaryCover;

    @BindView(R.id.tv_plant_name)
    TextView mPlantName;

    @BindView(R.id.tv_equipment_name)
    TextView mEquipmentName;

    @BindView(R.id.tv_location)
    TextView mPlantLocation;

    @BindView(R.id.tv_plant_time)
    TextView mPlantTime;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, DiaryContentActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_diary_content;
    }

    @OnClick(R.id.img_close_diary_content)
    void onCloseDiaryContentClick(){
        finish();
    }
}
