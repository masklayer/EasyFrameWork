package com.easyframework.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.widget.ImageView;

/**
 * imageView动画
 * @author zhangbp
 *
 */
final class PicassoDrawable extends BitmapDrawable {
	private static final float FADE_DURATION = 200f; // ms

	static void setBitmap(ImageView target, Context context, Bitmap bitmap, boolean fade) {
		Drawable placeholder = target.getDrawable();
		if (placeholder instanceof AnimationDrawable) {
			((AnimationDrawable) placeholder).stop();
		}
		PicassoDrawable drawable = new PicassoDrawable(context, bitmap, placeholder, fade);
		target.setImageDrawable(drawable);
	}

	static void setPlaceholder(ImageView target, Drawable placeholderDrawable) {
		target.setImageDrawable(placeholderDrawable);
		if (target.getDrawable() instanceof AnimationDrawable) {
			((AnimationDrawable) target.getDrawable()).start();
		}
	}


	Drawable placeholder;

	long startTimeMillis;
	boolean animating;
	int alpha = 0xFF;

	PicassoDrawable(Context context, Bitmap bitmap, Drawable placeholder, boolean fade) {
		super(context.getResources(), bitmap);

		if (fade) {
			this.placeholder = placeholder;
			animating = true;
			startTimeMillis = SystemClock.uptimeMillis();
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (!animating) {
			super.draw(canvas);
		} else {
			float normalized = (SystemClock.uptimeMillis() - startTimeMillis) / FADE_DURATION;
			if (normalized >= 1f) {
				animating = false;
				placeholder = null;
				super.draw(canvas);
			} else {
				if (placeholder != null) {
					placeholder.draw(canvas);
				}

				int partialAlpha = (int) (alpha * normalized);
				super.setAlpha(partialAlpha);
				super.draw(canvas);
				super.setAlpha(alpha);
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
					invalidateSelf();
				}
			}
		}
	}

	@Override
	public void setAlpha(int alpha) {
		this.alpha = alpha;
		if (placeholder != null) {
			placeholder.setAlpha(alpha);
		}
		super.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		if (placeholder != null) {
			placeholder.setColorFilter(cf);
		}
		super.setColorFilter(cf);
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		if (placeholder != null) {
			placeholder.setBounds(bounds);
		}
		super.onBoundsChange(bounds);
	}

}
