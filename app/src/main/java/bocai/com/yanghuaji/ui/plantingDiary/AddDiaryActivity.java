package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.media.GalleryFragment;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.ui.personalCenter.EquipmentListPopupWindow;
import bocai.com.yanghuaji.presenter.plantingDiary.AddDiaryContract;
import bocai.com.yanghuaji.presenter.plantingDiary.AddDiaryPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.persistence.Account;
import bocai.com.yanghuaji.util.widget.RoundAngleImageView;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/13.
 * 邮箱 yuanfei221@126.com
 */

public class AddDiaryActivity extends PresenterActivity<AddDiaryContract.Presenter> implements AddDiaryContract.View {
    @BindView(R.id.scroll_root)
    ScrollView mRoot;

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

    @BindView(R.id.img_add_cover)
    RoundAngleImageView mCover;

    @BindView(R.id.tv_equipment_name)
    TextView mEquipmentName;

    private String mCoverId;
    private String mName;
    private String mEquipmentId;


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
//        DiaryListActivity.show(this);
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                GlideApp.with(AddDiaryActivity.this)
                        .load(path)
                        .centerCrop()
                        .into(mCover);
                loadCover(path);
            }
        }).show(getSupportFragmentManager(), GalleryFragment.class.getName());
    }

    private void loadCover(String filePath) {
        Map<String, RequestBody> params = new HashMap<>();
        File file = new File(filePath);
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg;charset=utf-8"), file);
        params.put("pic_head" + "\";" + "filename=\"" + file, body);
        mPresenter.loadCover(params);
    }

    @OnClick(R.id.tv_equipment_name)
    void onEquipmentNameClick() {
        mPresenter.getEquipmentList(Account.getToken());
    }


    @OnClick(R.id.tv_right)
    void onSaveClick() {
        String token = Account.getToken();
        String coverId = mCoverId;
        String diaryName = mEditInputDiaryName.getText().toString();
        String equipmentId = mEquipmentId;
        String equipmentName = mName;
//        mPresenter.addDiary();
    }

    @Override
    public void addDiarySuccess() {

    }

    @Override
    public void loadCoverSuccess(String coverId) {
        mCoverId = coverId;
    }

    @Override
    public void getEquipmentListSuccess(List<EquipmentCard> cards) {
        if (cards !=null&&cards.size()>0){
            EquipmentListPopupWindow popupWindow = new EquipmentListPopupWindow(this);
            ActivityUtil.setBackgroundAlpha(this, 0.19f);
            popupWindow.addData(cards);
            popupWindow.showAtLocation(mRoot, Gravity.CENTER,0,0);
            popupWindow.setOnSelectListener(new EquipmentListPopupWindow.SelectListener() {
                @Override
                public void selected(EquipmentCard card) {
                    mName = card.getEquipName();
                    mEquipmentName.setText(mName);
                    mEquipmentId = card.getId();
                }
            });
        }else {
            Application.showToast("暂无设备");
        }

    }

    @Override
    protected AddDiaryContract.Presenter initPresenter() {
        return new AddDiaryPresenter(this);
    }
}
