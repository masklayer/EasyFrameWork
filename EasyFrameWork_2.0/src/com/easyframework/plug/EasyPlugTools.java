package com.easyframework.plug;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

/**
 * @author zhangbp
 *
 */
public class EasyPlugTools {
	
	public static String getDeviceId(Context mcontxt){
		TelephonyManager telephonyManager = (TelephonyManager )mcontxt.getSystemService( Context.TELEPHONY_SERVICE );
		return telephonyManager.getDeviceId();
	}
	
	public static String getVersionName(Context mContext){
		PackageInfo info;
		try {
			info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getPackageName(Context mContext){
		PackageInfo info;
		try {
			info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			return info.packageName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getAppName(Context mContext){
		PackageInfo info;
		try {
			info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			return info.applicationInfo.loadLabel(mContext.getPackageManager()).toString(); 
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static int getLayoutIdByName(Context mContext,String name){
		return mContext.getResources().getIdentifier(name, "layout", mContext.getPackageName());
	}
	
	public static int getDrawableIdByName(Context mContext,String name){
		return mContext.getResources().getIdentifier(name, "drawable", mContext.getPackageName());
	}
	
	public static int getIdByName(Context mContext,String name){
		return mContext.getResources().getIdentifier(name, "id", mContext.getPackageName());
	}
	
	/**
	 * 返回合适的单位
	 * @param bytes
	 * @return
	 * @author zhangbp
	 */
	public static String getSuitableSize(long bytes) {
		String fileSizeString = "";
		Double a = (double) (bytes / 1024);
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(2);
		if (a < 1024) {// K
			fileSizeString = f.format(a) + "K";
		} else if (a < 1024 * 1024) {// M
			a = a / 1024;
			fileSizeString = f.format(a) + "M";
		} else if (a < 1024 * 1024 * 1024) {// G
			a = a / (1024 * 1024);
			fileSizeString = f.format(a) + "G";
		}
		return fileSizeString;
	}
	
	/**
	 * 返回合适的单位-数量
	 * @param bytes
	 * @return
	 * @author zhangbp
	 */
	public static String getSuitableCount(int count) {
		String fileSizeString = count+"";
		if (count > 10000) {
			fileSizeString = (int)Math.rint(count/10000) + "万";
		}
		return fileSizeString;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String formatTime(long time, boolean exactly) {
		if (exactly) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.format(new Date(time));
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			return dateFormat.format(new Date(time));
		}
	}
	
	public static String md5(String string) {
	    byte[] hash;
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        if ((b & 0xFF) < 0x10) hex.append("0");
	        	hex.append(Integer.toHexString(b & 0xFF));
	    }

	    return hex.toString();
	}
	
	/**
	 * 判断是安装
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
		return false;
		try {
			context.getPackageManager().getApplicationInfo(packageName,PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	
	public static void openApk(Context context, String packageName){
		context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
	}
	
	/** 
	  * 把中文转成Unicode码 
	  * @param str 
	  * @return 
	  */  
	public static String chinaToUnicode(String str){  
	     String result="";  
	     for (int i = 0; i < str.length(); i++){  
	          int chr1 = (char) str.charAt(i);  
	          if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)  
	              result+="\\u" + Integer.toHexString(chr1);  
	          }else{  
	              result+=str.charAt(i);  
	          }  
	     }  
	     return result;  
	}

	/** 
	  * 判断是否为中文字符 
	  * @param c 
	  * @return 
	  */  
	public  static boolean isChinese(char c) {  
	     Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
	     if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
	            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
	            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
	            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
	            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
	            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
	        return true;  
	    }  
	    return false;  
	}
	
	/**
	 * 从apk里读取包名
	 * 
	 * @param apkPath
	 * @return
	 */
	public static String getPackageNameByApk(Context mContext,String apkPath) {
		PackageManager pm = mContext.getPackageManager();
		PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		if (packageInfo != null) {
			return packageInfo.packageName;
		}
		return null;
	}
	
	/**
	 * 从apk里读取包名
	 * 
	 * @param apkPath
	 * @return
	 */
	public static String[] getVersionByApk(Context mContext,String apkPath) {
		PackageManager pm = mContext.getPackageManager();
		PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		if (packageInfo != null) {
			return new String[]{packageInfo.versionName,packageInfo.versionCode+""};
		}
		return null;
	}
	
}
