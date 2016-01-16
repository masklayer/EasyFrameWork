package com.easyframework.plug;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import dalvik.system.DexClassLoader;

/**
 * @author zhangbp
 *
 */
public class EasyPlugApplication extends Application implements EasyPlug {
	private HashMap<String, Resources> mPlugResources = new HashMap<String, Resources>();
	private HashMap<String, Theme> mPlugTheme = new HashMap<String, Theme>();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		initPlug();
		super.onCreate();
	}

	@Override
	public Resources getResources(String packageName) {
		// TODO Auto-generated method stub
		return mPlugResources.get(packageName);
	}

	@Override
	public Theme getTheme(String packageName) {
		// TODO Auto-generated method stub
		return mPlugTheme.get(packageName);
	}

	/**
	 * 初始化插件
	 * 
	 * @throws Exception
	 */
	public void initPlug(){
		try{
			String[] rawPaths = super.getAssets().list("easy_plug");
			for(int i =0,len = rawPaths.length;i < len;i++){
				String path = rawPaths[i];
				path ="easy_plug/" + path;
				File outFile = new File(getFilesDir() + "/" + path);
				File plugDir = new File(outFile.getParent());
				if(!plugDir.exists()){
					plugDir.mkdirs();
				}
				if(!outFile.exists()){
					EasyPlugIoUtils.copyStream(super.getAssets().open(path), new FileOutputStream(outFile));
				}
			}
			disposeDex(getFilesDir()+"/easy_plug");
			disposeRes(getFilesDir()+"/easy_plug");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理dex
	 * 多个用":"分开
	 * @param dexPath
	 */
	private void disposeDex(String dexDir){
		try{
			File[] dexlists = new File(dexDir).listFiles();
			StringBuffer dexPaths = new StringBuffer();
			for(File dexPath : dexlists){
				String path = dexPath.getPath();
				if(!path.endsWith(".apk")) continue;
				dexPaths.append(path+":");
			}
			String dexPaths_str = (dexPaths.toString()+"~").replace(":~", "");
			if(dexPaths_str != null && !dexPaths_str.equals("")){
				DexClassLoader dcl = new DexClassLoader(dexPaths_str,dexDir, null,getClassLoader());
				diyClassLoader(dcl, getClassLoader());
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace(); 
		}
	}
	
	/**
	 * 处理资源
	 * @param paths
	 */
	private void disposeRes(String resDir){
		try {
			File[] fileLists = new File(resDir).listFiles();
			for (File mFile : fileLists) {
				String path = mFile.getPath();
				if(!path.endsWith(".apk")) continue;
				String packageName = EasyPlugTools.getPackageNameByApk(this, path);
				if (packageName != null) {
					AssetManager am = (AssetManager) AssetManager.class.newInstance();
					am.getClass().getMethod("addAssetPath", String.class).invoke(am, path);
					Resources superRes = super.getResources();
					Resources res = new Resources(am, superRes.getDisplayMetrics(), superRes.getConfiguration());
					Theme thm = res.newTheme();
					thm.setTo(super.getTheme());
					mPlugResources.put(packageName, res);
					mPlugTheme.put(packageName, thm);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 *　组合ClassLoader
	 * @param paramClassLoader1
	 * @param paramClassLoader2
	 * @return
	 */
	public static boolean diyClassLoader(ClassLoader paramClassLoader1, ClassLoader paramClassLoader2){
	    for (ClassLoader localClassLoader = paramClassLoader2; localClassLoader != null; localClassLoader = localClassLoader.getParent()){
	    	 if (localClassLoader == paramClassLoader1){
		        return true;
		      }
	    }
	    try{
	      Class<?> localClass = Class.forName("java.lang.ClassLoader");
	      Field localField = localClass.getDeclaredField("parent");
	      localField.setAccessible(true);
	      localField.set(paramClassLoader1, localField.get(paramClassLoader2));
	      localField.set(paramClassLoader2, paramClassLoader1);
	      return true;
	    }catch (Exception e) {
			// TODO: handle exception
		}
	    return false;
	  }
	

}
