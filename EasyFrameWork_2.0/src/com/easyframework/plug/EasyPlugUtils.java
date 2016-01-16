package com.easyframework.plug;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;


/**
 * @author zhangbp
 *
 */
public class EasyPlugUtils {

	public static void bindContext(Context mContext, String packageName) {
		setLayoutInflaterContext(LayoutInflater.from(mContext), packageName);
	}
	
	/**
	 * EasyContext
	 * @param mContext
	 * @param mResources
	 * @param mTheme
	 * @return
	 */
	public static EasyContext getEasyContext(Context mContext,String packageName){
		return new EasyContext(mContext,packageName);
	}
	
	/**
	 * 读取对应的LayoutInflater
	 * @param mContext
	 * @param packageName
	 * @return
	 */
	public static LayoutInflater getLayoutInflater(Context mContext,String packageName,boolean isNewLayoutInflater){
		
		LayoutInflater rawLayoutInflater = LayoutInflater.from(mContext);
		LayoutInflater newLayoutInflater = isNewLayoutInflater ? rawLayoutInflater.cloneInContext(mContext) : rawLayoutInflater;
		if(packageName != null && !packageName.equals("")){
			setLayoutInflaterContext(newLayoutInflater, packageName);
		}
		return newLayoutInflater;
	}
	
	/**
	 * 覆盖LayoutInflater的mContext
	 * @param mLayoutInflater
	 * @param mEasyContext
	 */
	private static void setLayoutInflaterContext(LayoutInflater mLayoutInflater,String packageName){
		
		try {
			Application mApplication = null;
			Context mContext = mLayoutInflater.getContext();
			if(mContext instanceof Activity){
				mApplication = ((Activity)mContext).getApplication();
			}else if(mContext instanceof Application){
				mApplication = (Application)mContext;
			}
			
			if(mApplication != null && mApplication instanceof EasyPlug && packageName != null){
				EasyContext mEasyContext = getEasyContext(mContext,packageName);
				
				Class<?> layoutInflaterClass = mLayoutInflater.getClass();
				while (layoutInflaterClass != LayoutInflater.class) {
					layoutInflaterClass = layoutInflaterClass.getSuperclass();
				}
				
				Field mContextField = layoutInflaterClass.getDeclaredField("mContext");
				mContextField.setAccessible(true);
				mContextField.set(mLayoutInflater,mEasyContext);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
