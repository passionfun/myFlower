package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
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

public class AddDiaryActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.tv_right)
    TextView mSave;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.et_diary_name)
    EditText mEditInputDiaryName;

    @BindView(R.id.et_planting_time)
    EditText mEditInputPlantingTime;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, AddDiaryActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_diary;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("添加日记本");
        mSave.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_add_cover)
    void onAddCoverClick() {
        DiaryListActivity.show(this);
    }

    @OnClick(R.id.tv_right)
    void onSaveClick() {

    }
}
