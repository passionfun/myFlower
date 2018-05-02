package bocai.com.yanghuajien.ui.plantingDiary;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.util.ActivityUtil;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class DiaryContentCardPopupWindow extends PopupWindow {
    private ImageView mDiaryCover,mClose;
    private TextView mPlantName,mEquipmentName,mLocation,mTime;
    private Context mContext;
    public DiaryContentCardPopupWindow(final Context context){
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_diary_content_card, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        mDiaryCover = view.findViewById(R.id.img_diary_cover);
        mClose = view.findViewById(R.id.img_close_diary_content);
        mPlantName = view.findViewById(R.id.tv_plant_name);
        mEquipmentName = view.findViewById(R.id.tv_equipment_name);
        mLocation = view.findViewById(R.id.tv_location);
        mTime = view.findViewById(R.id.tv_plant_time);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaryContentCardPopupWindow.this.dismiss();
            }
        });
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ActivityUtil.setBackgroundAlpha((Activity) context, 1f);
            }
        });
    }


    public void initData(String diaryCoverPath,String plantName,String equipmentName,String location,String time){
        Glide.with(mContext)
                .load(diaryCoverPath)
                .into(mDiaryCover);
        mPlantName.setText(plantName);
        mEquipmentName.setText(equipmentName);
        mLocation.setText(location);
        mTime.setText(time);
    }
}
