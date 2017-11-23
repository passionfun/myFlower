package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.util.ActivityUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class DiaryListActivity extends Activity {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.tv_diary_title)
    TextView mDiaryTitle;

    @BindView(R.id.ll_root)
    LinearLayout mRoot;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, DiaryListActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_diary_list;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_share)
    void onShareClick() {
        final ShareDiaryListPopupWindow popupWindow = new ShareDiaryListPopupWindow(this);
        popupWindow.setOnTtemClickListener(new ShareDiaryListPopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        popupWindow.dismiss();
                        break;
                    case R.id.img_copy_link:
                        // TODO 分享链接
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
    }

    @OnClick(R.id.img_data_card)
    void onDataCardClick() {
        DiaryContentCardPopupWindow popupWindow = new DiaryContentCardPopupWindow(this);
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        popupWindow.showAtLocation(mRoot, Gravity.BOTTOM,0,0);
    }

    @OnClick(R.id.img_diary_cover)
    void onDiaryCoverClick() {
        DiaryDetailActivity.show(this);
    }

    @OnClick(R.id.img_write_diary)
    void onWriteDiaryClick() {
        WriteDiaryActivity.show(this,"");
    }
}
