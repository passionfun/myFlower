package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.util.ActivityUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/13.
 * 邮箱 yuanfei221@126.com
 */

public class WriteDiaryActivity extends Activity {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.ll_write_diary)
    LinearLayout mRootLayout;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, WriteDiaryActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_write_diary;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_location)
    void onLocationClick() {
        LocationActivity.show(this);
    }

    @OnClick(R.id.img_add_picture)
    void onAddPictureClick() {
        final SelectPicPopupWindow picPopupWindow = new SelectPicPopupWindow(this);
        picPopupWindow.setOnTtemClickListener(new SelectPicPopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()){
                    case R.id.tv_take_photo:
                        // todo 拍摄照片
                        break;
                    case R.id.tv_from_gallery:
                        // todo 从相册选择照片
                        break;
                    case R.id.tv_cancel:
                        picPopupWindow.dismiss();
                        break;
                }
            }
        });
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        picPopupWindow.showAtLocation(mRootLayout, Gravity.BOTTOM,0,0);

    }

}
