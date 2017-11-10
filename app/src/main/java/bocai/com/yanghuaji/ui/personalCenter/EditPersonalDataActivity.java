package bocai.com.yanghuaji.ui.personalCenter;

import android.content.Context;
import android.content.Intent;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/10.
 * 邮箱 yuanfei221@126.com
 */

public class EditPersonalDataActivity extends Activity {


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, EditPersonalDataActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_edit_personal_data;
    }

    @OnClick(R.id.img_back)
    void onBackClick(){
        finish();
    }


    @OnClick(R.id.bt_save)
    void onSaveSubmit(){

    }

}
