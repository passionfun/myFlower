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

public class DiaryDetailActivity extends Activity {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.ll_root)
    LinearLayout mRoot;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, DiaryDetailActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_diary_detail;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_share)
    void onShareClick() {
        ShareDiaryContentPopupWindow popupWindow = new ShareDiaryContentPopupWindow(this);
        popupWindow.setOnTtemClickListener(new ShareDiaryContentPopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()) {
                    case R.id.img_save_picture:
                        // TODO 保存图片
                        break;
                    case R.id.img_share_qq:
                        // TODO 分享到QQ
                        break;
                    case R.id.img_share_wechat:
                        // TODO 分享到微信
                        break;
                    case R.id.img_share_friends:
                        // TODO 分享到朋友圈
                        break;
                }
            }
        });
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        popupWindow.showAtLocation(mRoot, Gravity.BOTTOM,0,0);
        popupWindow.initData("","hello world");
    }

    @OnClick(R.id.img_delete)
    void onDeleteClick() {

    }
}
