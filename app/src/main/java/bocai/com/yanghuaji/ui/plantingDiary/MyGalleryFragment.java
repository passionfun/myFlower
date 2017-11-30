package bocai.com.yanghuaji.ui.plantingDiary;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.media.GalleryFragment;
import bocai.com.yanghuaji.media.widget.GalleryView;
import bocai.com.yanghuaji.util.UiTool;

/**
 * 作者 yuanfei on 2017/11/23.
 * 邮箱 yuanfei221@126.com
 */

public class MyGalleryFragment extends BottomSheetDialogFragment
        implements GalleryView.SelectedChangeListener {

    private GalleryView mGallery;
    private MyGalleryFragment.OnSelectedListener mListener;
    private TextView mConfirm;
    private int maxCount;

    public MyGalleryFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = root.findViewById(R.id.galleryView);
        mConfirm = root.findViewById(R.id.tv_confirm);
        mConfirm.setVisibility(View.VISIBLE);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    // 得到所有的选中的图片的路径
                    String[] paths = mGallery.getSelectedPath();
                    mListener.onSelectedImage(paths);
                    mListener = null;
                }
                dismiss();
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(), this);
    }

    public void setMaxCount(int maxCount){
        this.maxCount = maxCount;
    }

    @Override
    public void onSelectedCountChanged(int count) {
//        mGallery.setMaxCount(maxCount);
    }


    /**
     * 设置事件监听，并返回自己
     */
    public MyGalleryFragment setListener(MyGalleryFragment.OnSelectedListener listener) {
        mListener = listener;
        return this;
    }


    /**
     * 选中图片的监听器
     */
    public interface OnSelectedListener {
        void onSelectedImage(String[] paths);
    }


    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if (window == null)
                return;

            int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
            int statusHeight = UiTool.getStatusBarHeight(getOwnerActivity());

            // 计算dialog的高度并设置
            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        }
    }

}
