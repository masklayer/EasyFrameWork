package com.easyframework.imageLoader.easyDrawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;

/**
 * 默认bitmap
 * 
 * @author zhangbp
 */

public class EasyDefaultBitmap extends BitmapDrawable {

	private long startTimeMillis;
	private int alpha = 0xFF;
	private boolean isFade = false;

	public EasyDefaultBitmap(Bitmap bitmap, Context context, boolean isFade) {
		// TODO Auto-generated constructor stub
		super(context.getResources(), bitmap);
		this.isFade = isFade;
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (isFade) {
			float normalized = (SystemClock.uptimeMillis() - startTimeMillis) / 200f;
			if (normalized < 1f) {
				int partialAlpha = (int) (alpha * normalized);
				setAlpha(partialAlpha);
				super.draw(canvas);
				invalidateSelf();
			} else {
				super.draw(canvas);
			}
		} else {
			super.draw(canvas);
		}
	}

}
