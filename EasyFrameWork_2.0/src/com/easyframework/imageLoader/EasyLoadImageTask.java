/**
 * 
 */
package com.easyframework.imageLoader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.easyframework.util.EasyIoUtils;
import com.easyframework.util.EasyLog;

/**
 * 图片加任务
 * @author zhangbp
 */

public class EasyLoadImageTask implements Runnable{
	
	private EasyImageLoadingInfo mEasyImageLoadingInfo;
	private EasyImageDecoder mEasyImageDecoder;
	private EasyImageCache mEasyImageCache;
	
	public EasyLoadImageTask(EasyImageLoadingInfo mEasyImageLoadingInfo){
		this.mEasyImageLoadingInfo = mEasyImageLoadingInfo;
		int[] imageSize = mEasyImageLoadingInfo.getImageSize();
		mEasyImageDecoder = new EasyImageDecoder(imageSize[0],imageSize[1],mEasyImageLoadingInfo.getDecodeConfig());
		mEasyImageCache = EasyImageLoader.getInstance().getEasyImageCache();
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		if(!checkTask()){
			return;
		}
		
		AtomicBoolean pause = EasyImageLoader.getInstance().getPause();
		
		if(pause.get()) {  //暂停状态
			synchronized (pause) {
				try {
					pause.wait();
				} catch (InterruptedException e) {
					
				}
			}
		}
		
		if(!checkTask()){
			return;
		}
		
		Bitmap bmp;
		try {

			bmp = loadBitmap();
			if(bmp != null){
				
				if(!checkTask()){
					return;
				}
				displayImage(bmp,false);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 尝试从内存中设置图片
	 * @return
	 * @author zhangbp
	 */
	public boolean setBibmapByMemory(){
		
		Bitmap bitmap = loadBitmapByMemoryCache();

		if(bitmap != null){
			displayImage(bitmap,true);
			return true;
		}else{
			int empid = mEasyImageLoadingInfo.getEmptyImage();
			ImageView imageview = mEasyImageLoadingInfo.getImageView();
			if(empid > 0 && imageview != null){
//				bitmap = BitmapFactory.decodeResource(imageview.getResources(), empid);
//				displayImage(bitmap,true);
				imageview.setImageResource(empid);
			}
		}
		return false;
	}
	
	/**
	 * 加载图片成功，回调显示
	 * @param bmp
	 * @author zhangbp
	 */
	
	private void displayImage(Bitmap bmp,boolean isMemory){
		EasyBitmapDisplayer mEasyBitmapDisplayer = mEasyImageLoadingInfo.getEasyBitmapDisplayer();
		mEasyBitmapDisplayer.display(mEasyImageLoadingInfo.getImageView(),bmp,isMemory);
	}
	
	/**
	 * 加载图片
	 * @return
	 * @author zhangbp (2013-5-27)
	 */
	private Bitmap loadBitmap() {
		Bitmap bitmap = loadBitmapByMemoryCache();
		if(bitmap == null){
			bitmap = loadBitmapByBytesCache();
			if(bitmap != null){
				saveToBitmapCache(bitmap);
			}
		}
		
		if(bitmap == null){
			bitmap = loadBitmapByDisc();
			if(bitmap != null){
				saveToBitmapCache(bitmap);
				saveToBytesCache(mEasyImageDecoder.getRawData());
			}
		}
		
		if(bitmap == null){
			bitmap = loadBitmapByNetWork();
			if(bitmap != null){
				saveToBitmapCache(bitmap);
				saveToBytesCache(mEasyImageDecoder.getRawData());
				saveToDisc(mEasyImageDecoder.getRawData());
			}
		}
		return bitmap;
	}
	
	/**
	 * 从内存中加载bitmap
	 * @author zhangbp (2013-11-24)
	 * @return Bitmap
	 */
	
	public Bitmap loadBitmapByMemoryCache(){
		Bitmap mBitmap = null; 
		if(mEasyImageCache != null){
			mBitmap = mEasyImageCache.getBitmap(mEasyImageLoadingInfo.getCacheKey());
		}
		
		if(mBitmap != null){
			EasyLog.i("命中内存缓存(bitmap),大小: "+mBitmap.getRowBytes() * mBitmap.getHeight() /1024f/1024f+"M");
		}
		return mBitmap;
		
	}
	
	
	/**
	 * 从内存缓存的图片字节中解析bitmap
	 * @author zhangbp (2013-12-22)
	 * @return Bitmap
	 */
	
	public Bitmap loadBitmapByBytesCache(){
		byte[] datas = null;
		if(mEasyImageCache != null){
			datas = mEasyImageCache.getBytes(mEasyImageLoadingInfo.getCacheKey());
		}
		Bitmap mBitmap = null;
		if(datas != null){
			mBitmap = mEasyImageDecoder.decode(datas);
			EasyLog.i("命中内存缓存(节字),大小: "+ datas.length /1024f/1024f+"M");
		}
		return mBitmap;
	}
	
	/**
	 * 从硬盘上解析bitmap
	 * @author zhangbp (2013-11-24)
	 * @return Bitmap
	 */
	
	private Bitmap loadBitmapByDisc(){
		File imageFile = new File(mEasyImageLoadingInfo.getDiscCacheDir() + File.separator + mEasyImageLoadingInfo.getCacheKey());
		Bitmap bitmap = null;
		try {
			if (imageFile.exists()) {
				bitmap = mEasyImageDecoder.decode(imageFile.toURI());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(bitmap != null){
			EasyLog.i("命中硬盘缓存,大小: "+imageFile.length() /1024f/1024f+"M");
		}
		return bitmap;
	}
	
	/**
	 * 从网络上解析bitmap
	 * @author zhangbp (2013-11-24)
	 * @return Bitmap
	 */
	
	private Bitmap loadBitmapByNetWork(){
		Bitmap bmp = null;
		try {
			bmp = mEasyImageDecoder.decode(new URI(mEasyImageLoadingInfo.getImageUri()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}
	
	/**
	 * 保存bitmap到缓存区(内存)
	 * @param mBitmap
	 */
	private void saveToBitmapCache(Bitmap mBitmap){
		if(mBitmap != null && mEasyImageCache != null){
			mEasyImageCache.putBitmap(mEasyImageLoadingInfo.getCacheKey(), mBitmap);
		}
	}
	
	/**
	 * 保存Bytes到缓存区(内存)
	 * @param mBitmap
	 */
	private void saveToBytesCache(byte[] bytes){
		if(bytes != null && mEasyImageCache != null){
			mEasyImageCache.putBytes(mEasyImageLoadingInfo.getCacheKey(), bytes);
		}
	}
	
	/**
	 * 保存到sdcard
	 * @param mBitmap
	 */
	private void saveToDisc(byte[] bytes){
		try{
			File imageFile = new File(mEasyImageLoadingInfo.getDiscCacheDir() + File.separator + mEasyImageLoadingInfo.getCacheKey());
			if(!imageFile.exists()){
				OutputStream os = new BufferedOutputStream(new FileOutputStream(imageFile));
				os.write(bytes);
				EasyIoUtils.closeSilently(os);
			}
		}catch (Exception e) {
			// TODO: handle exception
			EasyLog.w("保存图片失败");
		}
	}
	
	/**
	 * 检查任务是否有效
	 * @author zhangbp (2013-5-29)
	 */
	private boolean checkTask(){
		ImageView mImageView = mEasyImageLoadingInfo.getImageView();
		if(mImageView != null && mImageView.getTag(Integer.MAX_VALUE) != null){
			String tag = (String)mImageView.getTag(Integer.MAX_VALUE);
			return mEasyImageLoadingInfo.getImageUri().equals((String)tag);
		}else{
			return true;
		}
	}

}
