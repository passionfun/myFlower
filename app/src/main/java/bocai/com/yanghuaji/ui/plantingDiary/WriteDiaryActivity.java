package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.ImageModel;
import bocai.com.yanghuaji.presenter.plantingDiary.WriteDiaryContract;
import bocai.com.yanghuaji.presenter.plantingDiary.WriteDiaryPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.BitmapUtils;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/13.
 * 邮箱 yuanfei221@126.com
 */

public class WriteDiaryActivity extends PresenterActivity<WriteDiaryContract.Presenter> implements WriteDiaryContract.View {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.ll_write_diary)
    LinearLayout mRootLayout;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.et_content)
    EditText mContent;

    @BindView(R.id.tv_location)
    TextView mLocation;

    @BindView(R.id.img_add_picture)
    ImageView mAdd;

    public static final int TAKE_PHOTO_REQUEST_ONE = 0x001;
    public static final String KEY_DIARY_ID = "KEY_DIARY_ID";
    private String mDiaryId;
    private Uri imageUri;
    private RecyclerAdapter<String> mAdapter;

    //显示的入口
    public static void show(Context context, String diaryId) {
        Intent intent = new Intent(context, WriteDiaryActivity.class);
        intent.putExtra(KEY_DIARY_ID, diaryId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_write_diary;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mDiaryId = bundle.getString(KEY_DIARY_ID);
        return super.initArgs(bundle);

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<String>() {

            @Override
            public void add(String s) {
                super.add(s);
                if (mAdapter.getItems().contains("add")) {
                    mAdapter.getItems().remove("add");
                }
                mAdapter.getItems().add("add");
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void add(String... dataList) {
                super.add(dataList);
                if (mAdapter.getItems().contains("add")) {
                    mAdapter.getItems().remove("add");
                }
                mAdapter.getItems().add("add");
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected int getItemViewType(int position, String s) {
                return R.layout.item_diary_photos;
            }

            @Override
            protected ViewHolder<String> onCreateViewHolder(View root, int viewType) {
                return new WriteDiaryActivity.ViewHolder(root);
            }
        });
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<String>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, String s) {
                if (mAdapter.getItems().size() <= 9) {
                    if (mAdapter.getItemCount() == (holder.getAdapterPosition() + 1)) {
                        showPop();
                    }
                }
            }
        });
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_location)
    void onLocationClick() {
        LocationActivity.show(this);
    }


    @OnClick(R.id.tv_save)
    void onSaveClick() {
        List<String> photoPaths = mAdapter.getItems();
        photoPaths.remove("add");
        uploadPhotos(photoPaths);
    }


    @OnClick(R.id.img_add_picture)
    void onAddPictureClick() {
        showPop();
    }

    private void showPop() {
        final SelectPicPopupWindow picPopupWindow = new SelectPicPopupWindow(this);
        picPopupWindow.setOnTtemClickListener(new SelectPicPopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_take_photo:
                        picPopupWindow.dismiss();
                        //  拍摄照片
                        imageUri = BitmapUtils.createImageUri(WriteDiaryActivity.this);
                        Intent intent = new Intent();
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        //如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO_REQUEST_ONE);
                        break;
                    case R.id.tv_from_gallery:
                        picPopupWindow.dismiss();
                        // 从相册选择照片
                        MyGalleryFragment fragment = new MyGalleryFragment().setListener(new MyGalleryFragment.OnSelectedListener() {
                            @Override
                            public void onSelectedImage(String[] paths) {
                                mAdapter.add(paths);
                                switchVisibility(true);
                            }
                        });
                        fragment.show(getSupportFragmentManager(), MyGalleryFragment.class.getName());
                        fragment.setMaxCount(10 - mAdapter.getItemCount());
                        break;
                    case R.id.tv_cancel:
                        picPopupWindow.dismiss();
                        break;
                }
            }
        });
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        picPopupWindow.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 0);
    }


    private void uploadPhotos(List<String> pathList) {
        Map<String, RequestBody> params = new HashMap<>();


        if (pathList != null && pathList.size() > 0) {
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i));
                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg;charset=utf-8"), file);
                params.put("pic_head" + "\";" + "filename=\"" + file, body);
            }
            mPresenter.addPhotos(params);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            BitmapUtils.delteImageUri(WriteDiaryActivity.this, imageUri);
            return;
        }
        if (requestCode == TAKE_PHOTO_REQUEST_ONE) {
            mAdapter.add(imageUri + "");
            switchVisibility(true);
        }
    }

    private void switchVisibility(boolean isRecyclerVisibile) {
        if (isRecyclerVisibile) {
            mRecycler.setVisibility(View.VISIBLE);
            mAdd.setVisibility(View.INVISIBLE);
        } else {
            mRecycler.setVisibility(View.INVISIBLE);
            mAdd.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void addPhotosSuccess(List<ImageModel.AvatarBean> avatar) {
        String token = Account.getToken();
        String content = mContent.getText().toString();
        String location = mLocation.getText().toString();
        StringBuffer buffer = new StringBuffer();
        if (avatar != null && avatar.size() > 0) {
            for (ImageModel.AvatarBean avatarBean : avatar) {
                String photoId = avatarBean.getId() + "";
                buffer.append(photoId).append(",");
            }
        }
        String photosId = buffer.toString();
        String diaryId = mDiaryId;
        mPresenter.writeDiary(token, content, location, photosId, diaryId);
    }

    @Override
    public void writeDiarySuccess() {
        finish();
    }

    @Override
    protected WriteDiaryContract.Presenter initPresenter() {
        return new WriteDiaryPresenter(this);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<String> {
        @BindView(R.id.img_photo)
        ImageView photo;

        @BindView(R.id.img_delete)
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(String s) {

            if (s.equals("add")) {
                GlideApp.with(WriteDiaryActivity.this)
                        .load(R.mipmap.img_add_photos)
                        .centerCrop()
                        .into(photo);
                delete.setVisibility(View.GONE);
            } else {
                GlideApp.with(WriteDiaryActivity.this)
                        .load(s)
                        .centerCrop()
                        .into(photo);
                delete.setVisibility(View.VISIBLE);
            }
            //最多只能九张
            if (mAdapter.getItemCount() > 9) {
                if (s.equals("add")) {
                    photo.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }

            }
        }

        @OnClick(R.id.img_delete)
        void onDeleteClick() {
            mAdapter.remove(mAdapter.getItems().get(getAdapterPosition()));
        }
    }

}
