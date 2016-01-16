package com.easyframework.plug;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;

/**
 * Easy
 * @author zhangbp
 *
 */
public class EasyContext extends ContextWrapper{
	private Resources mResources;
	private Theme mTheme;
	
	public EasyContext(Context mContext,String packageName) {
		super(mContext);
		try {
			Application mApplication = null;
			if(mContext instanceof Activity){
				mApplication = ((Activity)mContext).getApplication();
			}else if(mContext instanceof Application){
				mApplication = (Application)mContext;
			}
			if(mApplication != null && mApplication instanceof EasyPlug && packageName != null){
				mResources = ((EasyPlug)mApplication).getResources(packageName);
				mTheme = ((EasyPlug)mApplication).getTheme(packageName);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Override
	public Resources getResources() {
		// TODO Auto-generated method stub
		return mResources == null ? super.getResources() : mResources;  
	}
	
	@Override
	public AssetManager getAssets() {
		// TODO Auto-generated method stub
		return mResources.getAssets();
	}
	
	@Override
	public Theme getTheme() {
		// TODO Auto-generated method stub
		return mTheme == null ? super.getTheme() : mTheme;
	}
	
}
