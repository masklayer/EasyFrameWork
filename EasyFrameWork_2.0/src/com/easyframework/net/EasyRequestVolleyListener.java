package com.easyframework.net;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.android.volley.VolleyError;
import com.easyframework.json.EasyJson;

/**
 * 异步任务回调
 * 
 * @author zhangbp
 * 
 * @param <T>
 */
public abstract class EasyRequestVolleyListener<T> {

	/**
	 * 请求发送前
	 * @param httpUrl
	 */
	public void onPre(String httpUrl) {

	}
	
	/**
	 * 加载成功－－子线程调用
	 * @param httpUrl
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T onProcess(String httpUrl, String data) {
		// TODO Auto-generated method stub
		try {
			if (data != null && !data.equals("")) {
				Type genType = getClass().getGenericSuperclass();
				Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
				if(params[0] != String.class){
					return EasyJson.toBean(data, params[0]);
				}else{
					return (T)data;
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 回调到UI线程
	 * @param httpUrl
	 * @param data
	 */
	public void onComplete(String httpUrl, Object data) {
		
	}
	
	/**
	 * 加载失败
	 * @param error
	 */
	public  void onErrorResponse(VolleyError error){
		
	}

}
