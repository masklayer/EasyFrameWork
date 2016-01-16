package com.easyframework.modules;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import android.database.sqlite.SQLiteOpenHelper;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import com.easyframework.db.EasyBaseDao;
import com.easyframework.imageLoader.EasyImageLoader;
import com.easyframework.json.EasyJson;
import com.easyframework.net.EasyRequest;
import com.easyframework.net.EasyRequestVolley;

public class EasyActionImpl {

	/**
	 * json到javaBean支持泛型
	 * 
	 * @param json
	 * @param mType
	 *            泛型类型（使用com.google.gson.reflect.TypeToken获得，例:Type typeOfT =
	 *            new TypeToken<Collection<Foo>>(){}.getType()）
	 * @return
	 * @author zhangbp
	 */
	public static <T> T jsonToBean(String json, Type type) {
		return EasyJson.toBean(json, type);
	}

	/**
	 * json到javaBean
	 * 
	 * @param json
	 * @param mClass
	 * @return
	 * @author zhangbp
	 */
	public static <T> T jsonToBean(String json, Class<T> mClass) {
		return EasyJson.toBean(json, mClass);
	}

	public static EasyImageLoader getEasyImageLoader() {
		// TODO Auto-generated method stub
		return EasyImageLoader.getInstance();
	}

	public static EasyBaseDao getEasyBaseDao(SQLiteOpenHelper mSQLiteOpenHelper) {
		// TODO Auto-generated method stub
		return new EasyBaseDao(mSQLiteOpenHelper);
	}

	/*
	 * public static EasyDownloadManager getEasyDownloadManager() { // TODO
	 * Auto-generated method stub return EasyDownloadManager.getInstance(); }
	 */

	public static EasyRequestVolley getEasyRequestVolley(int method, String url) {
		// TODO Auto-generated method stub
		return new EasyRequestVolley(method, url);
	}

	public static EasyRequest getEasyRequest(String url) {
		// TODO Auto-generated method stub
		return new EasyRequest(url);
	}

	/**
	 * 解析NetworkResponse
	 * @param mNetworkResponse
	 * @param mClass
	 * @return
	 */
	
	public static  <T> T paserNetworkResponse(NetworkResponse mNetworkResponse, Class<T> mClass) {
		String parsed;
		try {
			parsed = new String(mNetworkResponse.data, HttpHeaderParser.parseCharset(mNetworkResponse.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(mNetworkResponse.data);
		}
		
		if(parsed != null && !parsed.equals("")){
			return EasyJson.toBean(parsed, mClass);
		}
		return null;
	}
	
	/**
	 * 解析NetworkResponse
	 * @param mNetworkResponse
	 *  @param mType
	 *            泛型类型（使用com.google.gson.reflect.TypeToken获得，例:Type typeOfT =
	 *            new TypeToken<Collection<Foo>>(){}.getType()）
	 * @return
	 */
	
	public static  <T> T paserNetworkResponse(NetworkResponse mNetworkResponse, Type mType) {
		String parsed;
		try {
			parsed = new String(mNetworkResponse.data, HttpHeaderParser.parseCharset(mNetworkResponse.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(mNetworkResponse.data);
		}
		
		if(parsed != null && !parsed.equals("")){
			return EasyJson.toBean(parsed, mType);
		}
		return null;
	}
	

}
