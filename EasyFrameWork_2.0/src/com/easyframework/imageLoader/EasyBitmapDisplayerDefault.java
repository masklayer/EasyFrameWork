package com.easyframework.imageLoader;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.easyframework.imageLoader.easyDrawable.EasyDrawable;

/**
 * 默认,图片加载完回调
 * 
 * @author zhangbp
 * 
 */

class EasyBitmapDisplayerDefault implements EasyBitmapDisplayer {

	@Override
	public Bitmap display(final ImageView imageView, final Bitmap bitmap, final boolean isMemory) {
		// TODO Auto-generated method stub
		if (imageView == null || bitmap == null)
			return null;
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				imageView.setImageDrawable(EasyDrawable.drawDefaultBitmap(imageView.getContext(), bitmap, !isMemory));
				// imageView.setImageBitmap(bitmap);
			}
		});
		return bitmap;
	}

}
