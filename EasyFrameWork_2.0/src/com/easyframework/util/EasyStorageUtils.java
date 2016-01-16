
package com.easyframework.util;

import static android.os.Environment.MEDIA_MOUNTED;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

public final class EasyStorageUtils {

	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

	private EasyStorageUtils() {
	}

	/**
	 * 返回 应用的缓存目录 ，优先在sdcard上("/Android/data/[app_package_name]/cache"),需要sdcard操作权限,如果失败返回android系统的缓存目录
	 * @param context Application context
	 * @return Cache {@link File directory}
	 */
	public static File getCacheDirectory(Context context) {
		File appCacheDir = null;
		if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			EasyLog.w("获取应用缓存目录失败");
		}
		return appCacheDir;
	}

	/**
	 * 返回 应用的缓存目录
	 * @param context
	 * @param cacheDir
	 * @return
	 */
	public static File getOwnCacheDirectory(Context context, String cacheDir) {
		File appCacheDir = null;
		if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
			appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
		}
		if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

	/**
	 * 创建缓存目录和.nomedia文件
	 * @param context
	 * @return
	 */
	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				EasyLog.w("无法创建外部缓存目录");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				EasyLog.i("不能创建 .nomedia文件");
			}
		}
		return appCacheDir;
	}

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}
}
