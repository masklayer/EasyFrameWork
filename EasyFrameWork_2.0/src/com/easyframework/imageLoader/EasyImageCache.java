package com.easyframework.imageLoader;


import android.graphics.Bitmap;

/**
 * 图片缓存
 * @author zhangbp
 *
 */
public class EasyImageCache{
	private EasyImageBitmapCache mEasyImageBitmapCache;
	private EasyImageByteCache mEasyImageByteCache;
	
	/**
	 * @param bitmapCacheSize(bitmap对象缓存，单位byte)
	 * @param byteCacheSize(图片字节缓存，单位byte)
	 */
	public EasyImageCache(int bitmapCacheSize,int byteCacheSize){
		mEasyImageBitmapCache = new EasyImageBitmapCache(bitmapCacheSize);
		mEasyImageByteCache = new EasyImageByteCache(byteCacheSize);
	}
	
	public void putBitmap(String key, Bitmap mBitmap) {
		// TODO Auto-generated method stub
		mEasyImageBitmapCache.put(key, mBitmap);
	}

	public void putBytes(String key, byte[] mByte) {
		// TODO Auto-generated method stub
		mEasyImageByteCache.put(key, mByte);
	}

	public Bitmap getBitmap(String key) {
		// TODO Auto-generated method stub
		return mEasyImageBitmapCache.get(key);
	}

	public byte[] getBytes(String key) {
		// TODO Auto-generated method stub
		return mEasyImageByteCache.get(key);
	}
	

	/**
	 * Bitmap缓存
	 * EasyImageByteCache
	 * @author zhangbp
	 *
	 */
	
	class EasyImageBitmapCache extends EasyMemoryCache<String, Bitmap> {

		/**
		 * @param maxSize
		 */
		public EasyImageBitmapCache(int maxSize) {
			super(maxSize);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int sizeOf(String key, Bitmap value) {
			// TODO Auto-generated method stub
			if (value != null) {
				return value.getRowBytes() * value.getHeight();
			}
			return 0;
		}

	}
	
	/**
	 * 图片字节缓存
	 * EasyImageByteCache
	 * @author zhangbp
	 *
	 */
	 class EasyImageByteCache extends EasyMemoryCache<String, byte[]>{

		/**
		 * @param maxSize
		 */
		public EasyImageByteCache(int maxSize) {
			super(maxSize);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int sizeOf(String key, byte[] value) {
			// TODO Auto-generated method stub
			if(value != null){
				return value.length;
			}
			return 0;
		}
		
	}

}
