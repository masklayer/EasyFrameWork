package com.easyframework.imageLoader.easyDrawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

/**
 * 实现常见形状的Drawable
 * 
 * @author zhangbp
 */

public abstract class EasyDrawable extends Drawable {

	public final Paint mPaint;
	private long startTimeMillis;
	private int alpha = 0xFF;
	private boolean isFade = false;

	/**
	 * @param bitmap
	 * @param cornerRadius
	 * @param margin
	 * @param sx
	 *            (bitmap缩放比例)
	 * @param sy
	 *            (bitmap缩放比例)
	 * @author zhangbp
	 */

	public EasyDrawable(boolean isFade) {
		this.isFade = isFade;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		if (isFade) {
			startTimeMillis = SystemClock.uptimeMillis();
		}
	}

	@Override
	public void draw(Canvas canvas) {

		if (isFade) {
			float normalized = (SystemClock.uptimeMillis() - startTimeMillis) / 200f;
			if (normalized < 1f) {
				int partialAlpha = (int) (alpha * normalized);
				setAlpha(partialAlpha);
				easyDraw(canvas);
				invalidateSelf();
			} else {
				easyDraw(canvas);
			}
		} else {
			easyDraw(canvas);
		}
	}

	public abstract void easyDraw(Canvas canvas);

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}

	/**
	 * 圆角矩形
	 * 
	 * @param bitmap
	 * @param cornerRadius
	 * @param margin
	 * @param sx
	 * @param sy
	 * @param isFade
	 * @return 
	 */

	public static Drawable drawRoundRect(Bitmap bitmap, float cornerRadius,boolean isFade) {
		return new EasyRoundRect(bitmap, cornerRadius,isFade);
	}
	
	/**
	 * 圆形
	 * @param bitmap
	 * @param isFade
	 * @return 
	 */

	public static Drawable drawEasyCircle(Bitmap bitmap,boolean isFade) {
		return new EasyCircle(bitmap,isFade);
	}

	/**
	 * 默认的bitmap
	 * 
	 * @param bitmap
	 * @param cornerRadius
	 * @param margin
	 * @param sx
	 * @param sy
	 * @param isFade
	 * @return 
	 */

	public static Drawable drawDefaultBitmap(Context context, Bitmap bitmap, boolean isFade) {
		return new EasyDefaultBitmap(bitmap, context, isFade);
	}

}
