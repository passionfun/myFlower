package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;

/**
 * 作者 yuanfei on 2017/11/22.
 * 邮箱 yuanfei221@126.com
 */

public class AddPlantActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.tv_right)
    TextView mSkip;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, AddPlantActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_plant;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("添加植物");

    }
}
