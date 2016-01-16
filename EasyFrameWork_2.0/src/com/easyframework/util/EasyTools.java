package com.easyframework.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

/**
 * tools
 * 
 * @author zhangbp
 */

public class EasyTools {

	/**
	 * 生成MD5
	 * 
	 * @author zhangbp
	 */
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
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return hex.toString();

	}

	/**
	 * 读取manifest里的meta-data数据
	 * 
	 * @param mContext
	 * @param name
	 * @return
	 * @author zhangbp
	 */
	public static String getMetaData(Context mContext, String name) {
		ApplicationInfo appInfo;
		try {
			appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.metaData == null ? null : appInfo.metaData.getString(name);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取manifest里的meta-data数据
	 * 
	 * @param mContext
	 * @param name
	 * @return
	 * @author zhangbp
	 */
	public static int getMetaDataInt(Context mContext, String name) {
		ApplicationInfo appInfo;
		try {
			appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.metaData == null ? null : appInfo.metaData.getInt(name);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取versionCode
	 * 
	 * @param context
	 * @return
	 * @author zhangbp
	 */

	public static int getVersionCode(Context mContext) {
		try {
			PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 判断sdCard的状态
	 * 
	 * @author zhangbp
	 */
	public static boolean sdCardIsExsit() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 读取sdCard的路径
	 * 
	 * @author zhangbp
	 */
	public static String getSdCardPath() {
		if (sdCardIsExsit()) {
			return Environment.getExternalStorageDirectory().getPath();
		}
		return null;
	}

	/**
	 * dip转px
	 * 
	 * @author zhangbp
	 */

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转dip
	 * 
	 * @author zhangbp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 读取DeviceId
	 * 
	 * @author zhangbp
	 */
	public static String getDeviceId(Context mcontxt) {
		TelephonyManager telephonyManager = (TelephonyManager) mcontxt.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * 读取系统给应用分配的内存(MB)
	 * 
	 * @author zhangbp
	 * @return
	 */
	public static int getRunMemory(Context mcontxt) {
		return ((ActivityManager) mcontxt.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}

	/**
	 * 获取链接的后缀名
	 * 
	 * @param url
	 * @return
	 */
	public static String getSuffix(String url) {
		if (url != null && url.length() > 0) {
			Pattern pattern = Pattern.compile("\\S*[?]\\S*");
			Matcher matcher = pattern.matcher(url);

			String[] spUrl = url.split("/");
			int len = spUrl.length;
			String endUrl = len > 0 ? spUrl[len - 1] : url;
			if (matcher.find()) {
				endUrl = endUrl.split("\\?")[0];
			}

			if (endUrl.indexOf(".") != -1) {
				String[] suffix = endUrl.split("\\.");
				return suffix[suffix.length - 1];
			}
		}

		return "";

	}

	/**
	 * checkPermissions
	 * 
	 * @param context
	 * @param permission
	 * @return true or false
	 */
	public static boolean checkPermissions(Context context, String permission) {
		PackageManager localPackageManager = context.getPackageManager();
		return localPackageManager.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * Determine the current networking is WIFI
	 * 
	 * @param context
	 * @return
	 */
	public static boolean currentNoteworkTypeIsWIFI(Context context) {
		ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectionManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * Judge wifi is available
	 * 
	 * @param inContext
	 * @return
	 */
	public static boolean isWiFiActive(Context inContext) {
		if (checkPermissions(inContext, "android.permission.ACCESS_WIFI_STATE")) {
			Context context = inContext.getApplicationContext();
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
							return true;
						}
					}
				}
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Testing equipment networking and networking WIFI
	 * 
	 * @param context
	 * @return true or false
	 */
	public static boolean isNetworkAvailable(Context context) {
		if (checkPermissions(context, "android.permission.INTERNET")) {
			ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

	/**
	 * Get the current time format yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */

	@SuppressLint("SimpleDateFormat")
	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return localSimpleDateFormat.format(date);
	}

	/**
	 * get APPKEY
	 * 
	 * @param context
	 * @return appkey
	 */
	public static String getAppKey(Context paramContext) {
		if (paramContext == null) {
			return "";
		}
		String umsAppkey;
		try {
			PackageManager localPackageManager = paramContext.getPackageManager();
			ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(paramContext.getPackageName(), 128);
			if (localApplicationInfo != null) {
				String str = localApplicationInfo.metaData.getString("UMS_APPKEY");
				if (str != null) {
					umsAppkey = str;
					return umsAppkey.toString();
				}
			}
		} catch (Exception localException) {

		}
		return "";
	}

	/**
	 * get currnet activity's name
	 * 
	 * @param context
	 * @return
	 */
	public static String getActivityName(Context context) {
		if (context == null) {
			return "";
		}
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (checkPermissions(context, "android.permission.GET_TASKS")) {
			ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
			return cn.getShortClassName();
		} else {
			return "";
		}

	}

	/**
	 * get PackageName
	 * 
	 * @param context
	 * @return
	 */
	public static String getPackageName(Context context) {
		return context.getPackageName();
	}

	/**
	 * get OS number
	 * 
	 * @param context
	 * @return
	 */
	public static String getOsVersion(Context context) {
		String osVersion = "";
		if (checkPhoneState(context)) {
			osVersion = android.os.Build.VERSION.RELEASE;
			return osVersion;
		} else {
			return null;
		}
	}

	/**
	 * get deviceid
	 * 
	 * @param context
	 *            add <uses-permission android:name="READ_PHONE_STATE" />
	 * @return
	 */
	public static String getDeviceID(Context context) {
		if (context == null) {
			return "";
		}
		if (checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
			String deviceId = "";
			if (checkPhoneState(context)) {
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				deviceId = tm.getDeviceId();
			}
			if (deviceId != null) {
				return deviceId;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * check phone _state is readied ;
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkPhoneState(Context context) {
		PackageManager packageManager = context.getPackageManager();
		if (packageManager.checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName()) != 0) {
			return false;
		}
		return true;
	}

	/**
	 * get sdk number
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getSdkVersion(Context paramContext) {
		String osVersion = "";
		if (!checkPhoneState(paramContext)) {
			osVersion = android.os.Build.VERSION.RELEASE;
			return osVersion;
		} else {
			return null;
		}
	}

	/**
	 * Get the version number of the current program
	 * 
	 * @param context
	 * @return
	 */

	public static String getCurVersion(Context context) {
		String curversion = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			curversion = pi.versionName;
			if (curversion == null || curversion.length() <= 0) {
				return "";
			}
		} catch (Exception e) {

		}
		return curversion;
	}

	/**
	 * Get the current send model
	 * 
	 * @param context
	 * @return
	 */
	public static int getReportPolicyMode(Context context) {
		String str = context.getPackageName();
		SharedPreferences localSharedPreferences = context.getSharedPreferences("ums_agent_online_setting_" + str, 0);
		int type = localSharedPreferences.getInt("ums_local_report_policy", 0);
		return type;
	}

	/**
	 * Get the base station information
	 * 
	 * @throws Exception
	 */
	public static JSONObject getCellInfo(Context context) throws Exception {
		JSONObject cell = new JSONObject();
		TelephonyManager mTelNet = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		CellLocation mCellLocation = mTelNet.getCellLocation();
		GsmCellLocation location = null;
		if (mCellLocation instanceof GsmCellLocation) {
			location = (GsmCellLocation) mTelNet.getCellLocation();
		}
		if (location == null) {
			return null;
		}

		int mcc = 0, mnc = 0, cid = 0, lac = 0;

		String operator = mTelNet.getNetworkOperator();
		if (operator != null && !operator.equals("")) {
			mcc = Integer.parseInt(operator.substring(0, 3));
			mnc = Integer.parseInt(operator.substring(3));
			cid = location.getCid();
			lac = location.getLac();
		}

		cell.put("mcc", mcc);
		cell.put("mnc", mnc);
		cell.put("lac", lac);
		cell.put("cid", cid);

		return cell;
	}

	/**
	 * To determine whether it contains a gyroscope
	 * 
	 * @return
	 */
	public static boolean isHaveGravity(Context context) {
		SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if (manager == null) {
			return false;
		}
		return true;
	}

	/**
	 * Get the current networking
	 * 
	 * @param context
	 * @return WIFI or MOBILE
	 */
	public static String getNetworkType(Context context) {
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int type = manager.getNetworkType();
		String typeString = "UNKNOWN";
		if (type == TelephonyManager.NETWORK_TYPE_CDMA) {
			typeString = "CDMA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_EDGE) {
			typeString = "EDGE";
		}
		if (type == TelephonyManager.NETWORK_TYPE_EVDO_0) {
			typeString = "EVDO_0";
		}
		if (type == TelephonyManager.NETWORK_TYPE_EVDO_A) {
			typeString = "EVDO_A";
		}
		if (type == TelephonyManager.NETWORK_TYPE_GPRS) {
			typeString = "GPRS";
		}
		if (type == TelephonyManager.NETWORK_TYPE_HSDPA) {
			typeString = "HSDPA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_HSPA) {
			typeString = "HSPA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_HSUPA) {
			typeString = "HSUPA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_UMTS) {
			typeString = "UMTS";
		}
		if (type == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
			typeString = "UNKNOWN";
		}
		if (type == TelephonyManager.NETWORK_TYPE_1xRTT) {
			typeString = "1xRTT";
		}
		if (type == 11) {
			typeString = "iDen";
		}
		if (type == 12) {
			typeString = "EVDO_B";
		}
		if (type == 13) {
			typeString = "LTE";
		}
		if (type == 14) {
			typeString = "eHRPD";
		}
		if (type == 15) {
			typeString = "HSPA+";
		}

		return typeString;
	}

	/**
	 * Determine the current network type
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkTypeWifi(Context context) {
		// TODO Auto-generated method stub

		if (checkPermissions(context, "android.permission.INTERNET")) {
			ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cManager.getActiveNetworkInfo();

			if (info != null && info.isAvailable() && info.getTypeName().equals("WIFI")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * Get the current application version number
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		String versionName = "";
		try {
			if (context == null) {
				return "";
			}
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {

		}
		return versionName;
	}

	/**
	 * Set the output log
	 * 
	 * @param tag
	 * @param log
	 */

	public static void printLog(String tag, String log) {

	}

	@SuppressLint("DefaultLocale")
	public static String getNetworkTypeWIFI2G3G(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = cm.getActiveNetworkInfo();
		String type = info.getTypeName().toLowerCase();
		if (!type.equals("wifi")) {
			type = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getExtraInfo();
		}
		return type;

	}

	/**
	 * Get device name, manufacturer + model
	 * 
	 * @return device name
	 */
	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;

		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	/**
	 * Capitalize the first letter
	 * 
	 * @param s
	 *            model,manufacturer
	 * @return Capitalize the first letter
	 */
	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	/**
	 * 把中文转成Unicode码
	 * 
	 * @param str
	 * @return
	 */
	public static String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}
}
