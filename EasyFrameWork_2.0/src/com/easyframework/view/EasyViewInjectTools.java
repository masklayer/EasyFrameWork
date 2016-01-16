package com.easyframework.view;

import java.lang.reflect.Field;
import java.util.HashMap;

import android.view.View;
import android.widget.TextView;

/**
 * 视图注解
 * 
 * @author zhangbp
 * 
 */
public class EasyViewInjectTools {

	/**
	 * 给View赋值
	 * @param mView
	 * @param obj
	 */
	
	public static void inject(View mView, Object obj) {
		HashMap<Field, View[]> maps = parseAnnotation(mView, obj);
		if(maps != null){
			inject(maps, obj);
		}
	}

	/**
	 * 给View赋值
	 * @param maps
	 * @param obj
	 */
	
	public static void inject(HashMap<Field, View[]> maps, Object obj) {
		for (Field mField : maps.keySet()) {
			Object value = null;
			try {
				value = mField.get(obj);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			View[] mViews = maps.get(mField);
			for (View view : mViews) {
				bindValue(view, value);
			}
		}

	}

	/**
	 * 给View赋值
	 * @param mView
	 * @param value
	 * @author zhangbp
	 */

	public static void bindValue(View mView, Object value) {
		if (mView != null && value != null) {
			if (mView instanceof TextView) {
				((TextView) mView).setText(value.toString());
			}
		}
	}

	/**
	 * 解析java属性对应的View
	 * 
	 * @param mView
	 * @param obj
	 * @return
	 * @author zhangbp
	 */

	public static HashMap<Field, View[]> parseAnnotation(View mView, Object obj) {
		Field[] Fields = obj.getClass().getFields();
		HashMap<Field, View[]> maps = new HashMap<Field, View[]>();
		for (Field field : Fields) {
			if (field.isAnnotationPresent(EasyViewInject.class)) {
				int[] resIds = field.getAnnotation(EasyViewInject.class).value();
				View[] temViews = new View[resIds.length];
				maps.put(field, temViews);
				for (int i = 0, len = resIds.length; i < len; i++) {
					temViews[i] = mView.findViewById(resIds[i]);
				}
			}
		}
		return maps;
	}

}
