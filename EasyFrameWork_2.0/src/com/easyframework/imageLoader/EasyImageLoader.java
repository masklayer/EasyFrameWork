package com.easyframework.imageLoader;

import java.util.concurrent.atomic.AtomicBoolean;

import com.easyframework.util.EasyExecutorService;

import android.app.ActivityManager;
import android.content.Context;

/**
 * 
 * 图片加载－－单例模式
 * @author zhangbp
 *
 */
public class EasyImageLoader{
	
	private EasyImageCache mEasyImageCache;
	private static EasyImageLoader mEasyImageLoader;
	private EasyExecutorService mImageLoaderEngine;

	private final AtomicBoolean paused = new AtomicBoolean(false);
	private int bitmapCacheSize,bytesCacheSize;
	private Context mApplicationContext;
	
	private EasyImageLoader(){
		
	}
	
	/**
	 * 构建EasyImageLoader
	 * @param mApplicationContext
	 */
	public void create(Context mApplicationContext){
		if(mImageLoaderEngine == null){
			if(bitmapCacheSize == 0 || bytesCacheSize == 0){
				defaultCacheSize(mApplicationContext);
			}
			this.mApplicationContext = mApplicationContext.getApplicationContext();
			mEasyImageCache = new EasyImageCache(bitmapCacheSize, bytesCacheSize);
			mImageLoaderEngine = new EasyExecutorService(4,4);
		}
	}
	
	/**
	 * 摧毁EasyImageLoader
	 */
	
	public void destroy(){
		mEasyImageLoader = null;
	}
	
	public Context getApplicationContext(){
		return mApplicationContext;
	}
	
	public void loadImage(EasyImageLoadingInfo mEasyImageLoadingInfo) {
		// TODO Auto-generated method stub
		EasyLoadImageTask task = new EasyLoadImageTask(mEasyImageLoadingInfo);
		if(!task.setBibmapByMemory() && mImageLoaderEngine != null){
			mImageLoaderEngine.submit(task);
		}
	}
	
	/**
	 * 设置ImageCache
	 * @param mEasyImageCache
	 */
	public void setEasyImageCache(EasyImageCache mEasyImageCache){
		this.mEasyImageCache = mEasyImageCache;
	}
	
	/**
	 * 设置内存缓存的大小(单位：字节)
	 * @param bitmapSize
	 * @param bytesSize
	 * @return
	 */
	public EasyImageLoader setMemoryCacheSize(int bitmapSize,int bytesSize){
		this.bitmapCacheSize = bitmapSize;
		this.bytesCacheSize = bytesSize;
		return this;
	}
	
	
	/**
	 * 设置默认的缓存区大小
	 * @param mContext
	 */
	public void defaultCacheSize(Context mContext){
		int mMemory = ((ActivityManager)mContext.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		mMemory = mMemory * 1024 * 1024;
		bitmapCacheSize = (int) (mMemory * 0.12);
		bytesCacheSize = (int) (mMemory * 0.08);
	}
	
	/**
	 * 返回ImageCache
	 * @return
	 */
	public synchronized EasyImageCache getEasyImageCache(){
		return this.mEasyImageCache;
	}
	
	
	/**
	 * 暂停线程
	 * @param mImageView
	 * @param urlKey
	 * @author zhangbp (2013-5-27)
	 */
	void pause() {
		synchronized (paused) {
			paused.set(true);
		}
	}

	/**
	 * 恢复线程
	 * @param mImageView
	 * @param urlKey
	 * @author zhangbp (2013-5-27)
	 */
	
	void resume() {
		synchronized (paused) {
			paused.set(false);
			paused.notifyAll();
		}
	}
	
	/**
	 * 读取状态
	 * @param mImageView
	 * @param urlKey
	 * @author zhangbp (2013-5-27)
	 */
	
	AtomicBoolean getPause() {
		synchronized (paused) {
			return paused;
		}
	}
	
	public static synchronized EasyImageLoader getInstance(){
		if(mEasyImageLoader == null){
			mEasyImageLoader = new EasyImageLoader();
		}
		return mEasyImageLoader;
	}
	
}
