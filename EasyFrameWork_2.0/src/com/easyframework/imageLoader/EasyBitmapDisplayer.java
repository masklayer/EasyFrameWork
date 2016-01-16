
package com.easyframework.imageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;


/**
 * 图片加载完回调
 * @author zhangbp
 *
 */
public interface EasyBitmapDisplayer {
	Bitmap display(ImageView imageView,Bitmap bitmap,boolean isMemory);
}
