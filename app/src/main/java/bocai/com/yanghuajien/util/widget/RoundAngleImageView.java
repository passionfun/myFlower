package bocai.com.yanghuajien.util.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import bocai.com.yanghuajien.R;

/**
 * 作者 yuanfei on 2017/11/22.
 * 邮箱 yuanfei221@126.com
 */
@SuppressLint("AppCompatCustomView")
public class RoundAngleImageView extends ImageView{

    private Paint paint;
    /**
     * 这两个都是画圆的半径
     */
    private int roundWidth = 20;
    private int roundHeight = 20;
    private int roundHeightLeftUp;
    private int roundHeightRightUp;
    private int roundHeightLeftBottom;
    private int roundHeightRightBottom;
    private Paint paint2;

    public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
            roundWidth = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundWidth, roundWidth);
            roundHeight = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeight, roundHeight);
            roundHeightLeftUp = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeightLeftUp, roundHeightLeftUp);
            roundHeightRightUp = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeightRightUp, roundHeightRightUp);
            roundHeightLeftBottom = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeightLeftBottom, roundHeightLeftBottom);
            roundHeightRightBottom = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeightRightBottom, roundHeightRightBottom);
        } else {
            float density = context.getResources().getDisplayMetrics().density;
            roundWidth = (int) (roundWidth * density);
            roundHeight = (int) (roundHeight * density);
        }

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        drawLiftUp(canvas2);
        drawLiftDown(canvas2);
        drawRightUp(canvas2);
        drawRightDown(canvas2);
        canvas.drawBitmap(bitmap, 0, 0, paint2);
        bitmap.recycle();
    }

    private void drawLiftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, roundHeightLeftUp);
        path.lineTo(0, 0);
        path.lineTo(roundHeightLeftUp, 0);
        path.arcTo(new RectF(0, 0, roundHeightLeftUp * 2, roundHeightLeftUp * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLiftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight() - roundHeightLeftBottom);
        path.lineTo(0, getHeight());
        path.lineTo(roundHeightLeftBottom, getHeight());
        path.arcTo(new RectF(0, getHeight() - roundHeightLeftBottom * 2, roundHeightLeftBottom * 2, getHeight()), 90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth() - roundHeightRightBottom, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - roundHeightRightBottom);
        path.arcTo(new RectF(getWidth() - roundHeightRightBottom * 2, getHeight() - roundHeightRightBottom * 2, getWidth(), getHeight()), -0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), roundHeightRightUp);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - roundHeightRightUp, 0);
        path.arcTo(new RectF(getWidth() - roundHeightRightUp * 2, 0, getWidth(), 0 + roundHeightRightUp * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

}
