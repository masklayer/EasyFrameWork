package com.easyframework.net;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.easyframework.json.EasyJson;

/**
 * 异步任务回调
 * 
 * @author zhangbp
 * 
 * @param <T>
 */
public abstract class EasyRequestListener<T> {

	public void onPre(String httpUrl) {

	}

	public T onProcess(String httpUrl, HttpResponse mHttpResponse) {
		// TODO Auto-generated method stub
		try {
			if (mHttpResponse != null) {

				String data = EntityUtils.toString(mHttpResponse.getEntity());
				if (data != null && !data.equals("")) {
					Type genType = getClass().getGenericSuperclass();
					Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
					new EasyJson();
					return EasyJson.toBean(data, params[0]);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void onComplete(String httpUrl, T data) {

	}

}
