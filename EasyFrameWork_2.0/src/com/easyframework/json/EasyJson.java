package com.easyframework.json;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * EasyJson.java 创建于 2013-7-22 上午1:26:39
 * 
 * @author zhangbp (zhang86-vip@163.com)
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Reserved.
 */

public class EasyJson {

	/**
	 * json到javaBean支持泛型
	 * 
	 * @param JsonElement
	 * @param mType
	 *            泛型类型（使用com.google.gson.reflect.TypeToken获得，例:Type typeOfT =
	 *            new TypeToken<Collection<Foo>>(){}.getType()）
	 * @return
	 */
	public static <T> T toBean(JsonElement json, Type mType) {
		// TODO Auto-generated method stub
		try {
			Gson mGson = new Gson();
			return mGson.fromJson(json, mType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json到javaBean支持泛型
	 * 
	 * @param json
	 * @param mType
	 *            泛型类型（使用com.google.gson.reflect.TypeToken获得，例:Type typeOfT =
	 *            new TypeToken<Collection<Foo>>(){}.getType()）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(String json, Type mType) {
		// TODO Auto-generated method stub
		try {
			if(mType != String.class){
				Gson mGson = new Gson();
				return mGson.fromJson(json, mType);
			}else{
				return (T) json;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * json到javaBean
	 * 
	 * @param json
	 * @param mClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(JsonElement json, Class<T> mClass) {
		// TODO Auto-generated method stub

		try {
			if(mClass != String.class){
				Gson mGson = new Gson();
				return mGson.fromJson(json, mClass);
			}else{
				return (T) json;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * json到javaBean
	 * 
	 * @param json
	 * @param mClass
	 * @return
	 */

	public static <T> T toBean(String json, Class<T> mClass) {
		// TODO Auto-generated method stub
		try {
			Gson mGson = new Gson();
			return mGson.fromJson(json, mClass);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
		
	}

	/**
	 * javaBean到json
	 * 
	 * @param Object
	 * @return
	 */

	public static String toJson(Object mObject) {
		// TODO Auto-generated method stub
		try {
			Gson mGson = new Gson();
			return mGson.toJson(mObject);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * javaBean到json支持泛型
	 * 
	 * @param mObject
	 * @param mType
	 *            泛型类型（使用com.google.gson.reflect.TypeToken获得，例:Type typeOfT =
	 *            new TypeToken<Collection<Foo>>(){}.getType()）
	 * @return
	 */

	public static String toJson(Object mObject, Type typeOfSrc) {
		// TODO Auto-generated method stub
		
		try {
			Gson mGson = new Gson();
			return mGson.toJson(mObject, typeOfSrc);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}