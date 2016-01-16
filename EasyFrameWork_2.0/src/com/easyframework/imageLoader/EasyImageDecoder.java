package com.easyframework.imageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

import com.easyframework.util.EasyIoUtils;
import com.easyframework.util.FlushedInputStream;

/**
 * 解码图片
 * 
 * @author zhangbp
 */

class EasyImageDecoder {

	private  int targetWidth;
	private  int targetHeight;
	public final String SCHEME_HTTP = "http";
	public final String SCHEME_HTTPS = "https";
	public final String SCHEME_FILE = "file";
	private final int BUFFER_SIZE = 8 * 1024; // 20 Kb
	private final Config mDecodeConfig;
	private byte[] rawData;
	private int request_count;

	EasyImageDecoder(int targetWidth, int targetHeight, Config mDecodeConfig) {
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
		this.mDecodeConfig = mDecodeConfig;
	}

	/**
	 * 根据URI解析bitmap
	 * @return
	 * @throws IOException
	 * @author zhangbp (2013-5-27)
	 */
	public Bitmap decode(URI imageUri) throws IOException {
		byte[] datas = getBytes(imageUri);
		return decode(datas);
	}
	
	/**
	 * 根据bytes解析bitmap
	 * @return
	 * @throws IOException
	 * @author zhangbp (2013-5-27)
	 */
	public Bitmap decode(byte[] datas){
		rawData = datas;
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		Bitmap bitmap = null;
		if (targetWidth == 0 && targetHeight == 0) {
			decodeOptions.inPreferredConfig = mDecodeConfig;
			bitmap = BitmapFactory.decodeByteArray(rawData, 0, rawData.length, decodeOptions);
		} else {
			decodeOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(rawData, 0, rawData.length, decodeOptions);
			targetWidth = (int) (targetWidth > 0 ? targetWidth :  decodeOptions.outWidth * ((float)targetHeight / decodeOptions.outHeight));
			targetHeight = (int) (targetHeight > 0 ? targetHeight :  decodeOptions.outHeight * ((float)targetWidth / decodeOptions.outWidth));
			decodeOptions.inJustDecodeBounds = false;
			decodeOptions.inSampleSize = findBestSampleSize(decodeOptions.outWidth, decodeOptions.outHeight, targetWidth, targetHeight);
			Bitmap tempBitmap = BitmapFactory.decodeByteArray(rawData, 0, rawData.length, decodeOptions);

			if (tempBitmap != null && (tempBitmap.getWidth() > targetWidth || tempBitmap.getHeight() > targetHeight)) {
				bitmap = Bitmap.createScaledBitmap(tempBitmap, targetWidth, targetHeight, true);
				tempBitmap.recycle();
			} else {
				bitmap = tempBitmap;
			}
		}
		return bitmap;
	}
	
	public byte[] getRawData(){
		return rawData;
	}

	/**
	 * 计算合适的缩放倍数
	 * 
	 * @param actualWidth
	 * @param actualHeight
	 * @param desiredWidth
	 * @param desiredHeight
	 * @return
	 */
	private int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0f;
		while ((n * 2) <= ratio) {
			n *= 2;
		}
		return (int) n;
	}

	/**
	 * 根据URi 返回 byte[]
	 * 
	 * @param imageUri
	 * @return
	 * @throws IOException
	 * @author zhangbp (2013-5-27)
	 */
	public byte[] getBytes(URI imageUri){
		String scheme = imageUri.getScheme();
		FlushedInputStream in = null;
		ByteArrayOutputStream swapStream = null;
		try{
			request_count += 1;
			if (SCHEME_HTTP.equals(scheme) || SCHEME_HTTPS.equals(scheme)) {
				HttpURLConnection conn = (HttpURLConnection) imageUri.toURL().openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setReadTimeout(20 * 1000);
				in = new FlushedInputStream(conn.getInputStream(), BUFFER_SIZE);
			} else if (SCHEME_FILE.equals(scheme)) {
				in = new FlushedInputStream(imageUri.toURL().openStream(), BUFFER_SIZE);
			}

			if (in != null) {
				swapStream = new ByteArrayOutputStream();
				EasyIoUtils.copyStream(in, swapStream);
				return swapStream.toByteArray();
			}

		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			if(request_count == 1){
				getBytes(imageUri);
			}
		}finally{
			EasyIoUtils.closeSilently(in);
			EasyIoUtils.closeSilently(swapStream);
		}
		
		return null;
	}

}