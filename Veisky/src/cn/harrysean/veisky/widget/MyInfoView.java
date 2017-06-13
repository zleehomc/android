package cn.harrysean.veisky.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.renderscript.Font;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;

public class MyInfoView extends View {

	private Paint mPaint;
	// private static final String mText = "drawText";
	private String mText = "drawText";

	public MyInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		// FILL填充, STROKE描边,FILL_AND_STROKE填充和描边
		mPaint.setStyle(Style.FILL);
		canvas.drawRect(new Rect(10, 10, 100, 100), mPaint);// 画一个矩形

		mPaint.setColor(Color.GREEN);
		mPaint.setTextSize(35.0f);
		canvas.drawText(mText, 10, 60, mPaint);
	}

}