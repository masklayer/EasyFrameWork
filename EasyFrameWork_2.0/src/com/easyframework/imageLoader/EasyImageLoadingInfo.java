package com.easyframework.imageLoader;

import java.io.File;

import com.easyframework.util.EasyStorageUtils;
import com.easyframework.util.EasyTools;

import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * 加载图片的信息
 * 
 * @author zhangbp
 * 
 */
public class EasyImageLoadingInfo {

	private String MEMORY_CACHE_KEY_FORMAT = "Easy_%1$s";

	private String uri;
	private String mCacheKey;
	private ImageView imageView;
	private int[] imageSize;
	private Config bitMapConfig;
	private int emptyImageResId;
	private EasyBitmapDisplayer mEasyBitmapDisplayer;
	private String discCacheDir;

	public EasyImageLoadingInfo(String uri, ImageView imageView) {
		if (uri != null && !uri.equals("")) {
			this.uri = Uri.encode(uri, "@#&=*+-_.,:!?()/~'%");
			this.imageView = imageView;
			this.mCacheKey = generateKey(uri);
			if(this.imageView != null){
				this.imageView.setTag(Integer.MAX_VALUE, this.uri);
			}
		}
	}

	public EasyImageLoadingInfo(String uri) {
		this(uri, null);
	}

	/**
	 * 返回ImageUri
	 * 
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public String getImageUri() {
		return this.uri;
	}

	/**
	 * 返回ImageUri
	 * 
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public ImageView getImageView() {
		return this.imageView;
	}

	/**
	 * 设置加载失败的图片---默认图片
	 * 
	 * @param resid
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public EasyImageLoadingInfo setEmptyImage(int resid) {
		this.emptyImageResId = resid;
		return this;
	}

	/**
	 * 返回空图片的resId
	 * 
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public int getEmptyImage() {
		return emptyImageResId;
	}

	/**
	 * 设置图片保存的目录
	 * 
	 * @param resid
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public EasyImageLoadingInfo setDiscCacheDir(String discCacheDir) {
		this.discCacheDir = discCacheDir;
		return this;
	}

	/**
	 * 返回图片保存的目录
	 * 
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public String getDiscCacheDir() {
		if (discCacheDir == null && imageView != null) {
			File mFile = EasyStorageUtils.getCacheDirectory(imageView.getContext().getApplicationContext());
			if (mFile != null) {
				discCacheDir = mFile.getPath();
			}
		}
		return discCacheDir;
	}

	/**
	 * 设置图片的尺寸
	 * 
	 * @param w
	 *            ,h
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public EasyImageLoadingInfo setImageSize(int w, int h) {
		this.imageSize = new int[] { w, h };
		return this;
	}

	/**
	 * 取图片控件的大小
	 * 
	 * @param imageView
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public int[] getImageSize() {
		if (imageSize == null) {
			imageSize = new int[] { 0, 0 };
			if (imageView != null) {
				LayoutParams params = imageView.getLayoutParams();
				int width = params.width;
				if (width <= 0)
					width = imageView.getMeasuredWidth();

				int height = params.height;
				if (height <= 0)
					height = imageView.getMeasuredHeight();
				imageSize = new int[] { width, height };
			}
		}
		return imageSize;

	}

	/**
	 * 设置bitmap-config
	 * @param bitMapConfig
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	
	public EasyImageLoadingInfo setDecodeConfig(Config bitMapConfig) {
		this.bitMapConfig = bitMapConfig;
		return this;
	}

	/**
	 * 返回bitmap-config
	 * 
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public Config getDecodeConfig() {
		return bitMapConfig != null ? bitMapConfig : Config.RGB_565;
	}

	/**
	 * 设置EasyBitmapDisplayer，重写EasyBitmapDisplayer接口可以用来处理图片
	 * 
	 * @param mEasyBitmapDisplayer
	 * @return
	 * @author zhangbp (2013-5-16)
	 */

	public EasyImageLoadingInfo setEasyBitmapDisplayer(EasyBitmapDisplayer mEasyBitmapDisplayer) {
		this.mEasyBitmapDisplayer = mEasyBitmapDisplayer;
		return this;
	}

	/**
	 * 返回EasyBitmapDisplayer
	 * 
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public EasyBitmapDisplayer getEasyBitmapDisplayer() {
		if (mEasyBitmapDisplayer == null) {
			mEasyBitmapDisplayer = new EasyBitmapDisplayerDefault();
		}
		return mEasyBitmapDisplayer;
	}

	/**
	 * 根据url生成key
	 * 
	 * @param imageUri
	 * @param targetSize
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public String generateKey(String imageUri) {
		return String.format(MEMORY_CACHE_KEY_FORMAT, EasyTools.md5(imageUri));
	}

	/**
	 * 返回cacheKey
	 * 
	 * @return
	 * @author zhangbp (2013-5-16)
	 */
	public String getCacheKey() {
		return this.mCacheKey;
	}

	/**
	 * 加载图片
	 * @author zhangbp
	 */
	public void loadImage() {
		if (uri != null) {
			EasyImageLoader.getInstance().loadImage(this);
		}
	}

}
