package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public class GroupManagerActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.img_delete)
    ImageView mDelete;

    @BindView(R.id.tv_edit)
    TextView mEdit;

    @BindView(R.id.tv_confirm)
    TextView mConfirm;

    @BindView(R.id.img_open_manager)
    ImageView mOpenManager;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, GroupManagerActivity.class));
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_manager;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("分组管理");
    }


    @OnClick(R.id.img_open_manager)
    void onOpenManagerClick() {
        //打开编辑
        mDelete.setVisibility(View.VISIBLE);
        mEdit.setVisibility(View.VISIBLE);
        mConfirm.setVisibility(View.VISIBLE);
        mOpenManager.setVisibility(View.GONE);
    }


    @OnClick(R.id.tv_confirm)
    void onConfirmClick() {
        //完成
        mDelete.setVisibility(View.GONE);
        mEdit.setVisibility(View.GONE);
        mConfirm.setVisibility(View.GONE);
        mOpenManager.setVisibility(View.VISIBLE);
    }


}
