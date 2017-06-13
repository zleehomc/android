package cn.harrysean.veisky;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;

public class ViewStartScreen extends SurfaceView implements Callback, Runnable {// 备注1
	private SurfaceHolder sfh;
	private Thread th;
	private Canvas canvas;
	private Paint paint, p;
	private int ScreenW, ScreenH;
	private int picalpha=0;
	private Resources res;
	private Bitmap bmp;
	private String copyright;
	public ViewStartScreen(Context context) {
		super(context);
		th = new Thread(this);
		sfh = this.getHolder();
		sfh.addCallback(this); // 备注1
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		p = new Paint();
		p.setColor(Color.YELLOW);
		p.setAntiAlias(true);
		this.setKeepScreenOn(true);// 保持屏幕常亮
		res = this.getResources();
		bmp = BitmapFactory.decodeResource(res, R.drawable.scstudio);
		copyright = res.getText(R.string.copyright).toString();
	}
	@Override
	public void startAnimation(Animation animation) {
		super.startAnimation(animation);
	}
	public void surfaceCreated(SurfaceHolder holder) {
		ScreenW = this.getWidth();// 备注2
		ScreenH = this.getHeight();
		th.start();
	}
	private void draw() {
		try {
			canvas = sfh.lockCanvas(); // 得到一个canvas实例
			canvas.drawColor(Color.WHITE);// 刷屏
			// TextPaint paint = TextView.getPaint();
			Rect rect = new Rect();

			paint.getTextBounds(copyright, 0, copyright.length(), rect);
			canvas.drawText(copyright, (ScreenW - rect.width()) / 2, (ScreenH - rect.height()), paint);
			Rect src = new Rect();
			Rect dst = new Rect();
			int w = (int) (bmp.getWidth() * 0.8), h = (int) (0.8 * bmp.getHeight());
			src.set(0, 0, bmp.getWidth(), bmp.getHeight());
			dst.set((ScreenW - w) / 2, ScreenH / 4, (ScreenW - w) / 2 + w, ScreenH / 4 + h);
				p.setAlpha(picalpha);
			canvas.drawBitmap(bmp, src, dst, p);
			picalpha=picalpha<235?picalpha+20:255;

		} catch (Exception e) {
		} finally { // 备注3
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas); // 将画好的画布提交
		}
	}
	public void run() {
		while (true) {
			draw();
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}
}
