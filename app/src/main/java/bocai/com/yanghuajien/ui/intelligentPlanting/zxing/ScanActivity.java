package bocai.com.yanghuajien.ui.intelligentPlanting.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Activity;
import butterknife.OnClick;

/**
 *
 * Created by shc on 2018/1/7.
 */

public class ScanActivity extends Activity  {
    private CaptureFragment captureFragment;
    public static final int REQUEST_IMAGE = 112;
    public static boolean isOpen = false;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_scan;
    }



    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initView();
    }

    private void initView() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.flashLightLayout);
        LinearLayout albumLayout = (LinearLayout) findViewById(R.id.albumLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }

            }
        });
        albumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mOnResultCallback.result(requestCode,resultCode,data);
        finish();
    }




    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            ScanActivity.this.setResult(RESULT_OK, resultIntent);
            ScanActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            ScanActivity.this.setResult(RESULT_OK, resultIntent);
            ScanActivity.this.finish();
        }
    };
    private static OnResultCallback mOnResultCallback;
    public static void SetOnResultCallback(OnResultCallback callback){
        mOnResultCallback = callback;
    }
   public interface OnResultCallback{
        void result(int requestCode, int resultCode, Intent data);
    }
}
