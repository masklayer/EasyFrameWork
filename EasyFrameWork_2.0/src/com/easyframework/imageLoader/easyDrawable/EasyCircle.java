package com.easyframework.imageLoader.easyDrawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * 圆形
 * 
 * @author zhangbp
 */

public class EasyCircle extends EasyDrawable {
	private final RectF mRect = new RectF();
	private final BitmapShader mBitmapShader;
	private final Paint mPaint;
	private Bitmap bitmap;

	public EasyCircle(Bitmap bitmap,boolean isFade) {
		super(isFade);
		// TODO Auto-generated constructor stub
		mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(mBitmapShader);
		this.bitmap = bitmap;
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		mRect.set(0, 0, bounds.width(), bounds.height());
		float sx = mRect.width() / Float.valueOf(bitmap.getWidth());
		float sy = mRect.height() / Float.valueOf(bitmap.getHeight());
		Matrix mMatrix = new Matrix();
		mMatrix.setScale(sx, sy);
		mBitmapShader.setLocalMatrix(mMatrix);
	}

	@Override
	public void easyDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		float x = mRect.width() / 2.0f;
		float y = mRect.height() / 2;
		float radius = Math.min(x, y);
		canvas.drawCircle(x, y, radius, mPaint);
	}

}
