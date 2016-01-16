package com.easyframework.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

/**
 * EasyRequest
 * @author zhangbp
 * 
 */

public class EasyRequestVolley extends Request<String>{
	
	private static RequestQueue mRequestQueue = null;
	private EasyRequestVolleyListener<?> mEasyRequestListener;
	private Object objData;
	private HashMap<String, String> mHeader;
	private EasyRequestParameters mEasyRequestParameters;
	private List<EasyFilePart> fileList;
	private String bodyContentType;
	
	public EasyRequestVolley(int method, String url) {
		super(method, url, null);
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        
        if(mEasyRequestListener != null){
        	objData = mEasyRequestListener.onProcess(getUrl(),parsed);
		}
        
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(String response) {
		// TODO Auto-generated method stub
		if(mEasyRequestListener != null){
			mEasyRequestListener.onComplete(getUrl(), objData);
		}
	}
	
	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError) {
		// TODO Auto-generated method stub
		return super.parseNetworkError(volleyError);
	}
	
	@Override
	public void deliverError(VolleyError error) {
		// TODO Auto-generated method stub
		if(mEasyRequestListener != null){
			mEasyRequestListener.onErrorResponse(error);
		}
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
		if(this.mHeader != null){
			return this.mHeader;
		}
		return super.getHeaders();
	}

	@Override
	public String getBodyContentType() {
		// TODO Auto-generated method stub
		if(bodyContentType == null){
			List<EasyFilePart> fileList = getUploadFiles();
			bodyContentType = fileList != null && fileList.size() > 0 ? "multipart/form-data;boundary=" + EasyFormEntity.BOUNDARY : super.getBodyContentType();
		}
		return bodyContentType;
	}
	
	
	@Override
	public byte[] getBody() throws AuthFailureError {
		// TODO Auto-generated method stub
		List<EasyFilePart> fileList = getUploadFiles();
		if(fileList != null && fileList.size() > 0){
			return  new EasyFormEntity(mEasyRequestParameters, fileList).getByte();
		}else if(mEasyRequestParameters != null){
			return mEasyRequestParameters.getRquestParam();
		}
		return super.getBody();
	}
	
	
	/**
	 * 设置request的类型
	 * 
	 * @param contentType
	 * @return
	 */
	public EasyRequestVolley setBodyContentType(String contentType) {
		this.bodyContentType = contentType;
		return this;
	}
	
	
	/**
	 * 设置request的header
	 * 
	 * @param mHeader
	 * @return
	 */
	public EasyRequestVolley setHeader(HashMap<String, String> mHeader) {
		this.mHeader = mHeader;
		return this;
	}
	
	/**
	 * 添加request的header
	 * @param mHeader
	 * @return
	 */
	public EasyRequestVolley addHeader(String key,String value) {
		mHeader = mHeader == null ? new HashMap<String, String>() : mHeader;
		mHeader.put(key, value);
		return this;
	}
	
	/**
	 * 添加request的header
	 * @param mHeader
	 * @return
	 */
	public EasyRequestVolley addHeader(HashMap<String, String> mHeader) {
		this.mHeader = this.mHeader == null ? new HashMap<String, String>() : this.mHeader;
		this.mHeader.putAll(mHeader);
		return this;
	}
	
	/**
	 * 设置参数
	 * @param mEasyRequestParameters
	 * @return
	 */
	public EasyRequestVolley setParameters(EasyRequestParameters mEasyRequestParameters) {
		this.mEasyRequestParameters = mEasyRequestParameters;
		return this;
	}
	
	/**
	 * 添加参数
	 * @param mEasyRequestParameters
	 * @return
	 */
	
	public EasyRequestVolley addParameters(EasyRequestParameters mEasyRequestParameters) {
		this.mEasyRequestParameters = this.mEasyRequestParameters == null ? new EasyRequestParameters() : this.mEasyRequestParameters;
		this.mEasyRequestParameters.putAll(mEasyRequestParameters);
		return this;
	}
	
	/**
	 * 添加参数
	 * @param mEasyRequestParameters
	 * @return
	 */
	
	public EasyRequestVolley addParameters(String key,String value) {
		this.mEasyRequestParameters = this.mEasyRequestParameters == null ? new EasyRequestParameters() : this.mEasyRequestParameters;
		this.mEasyRequestParameters.put(key,value);
		return this;
	}
	
	/**
	 * 添加上传的文件，请用post方法
	 * @param mEasyFilePart
	 * @return
	 */
	
	public EasyRequestVolley addFile(EasyFilePart mEasyFilePart) {
		if (fileList == null) {
			fileList = new ArrayList<EasyFilePart>();
		}
		fileList.add(mEasyFilePart);
		return this;
	}
	
	/**
	 * 返回上传文件列表
	 * 
	 * @return
	 */
	public List<EasyFilePart> getUploadFiles() {
		return this.fileList;
	}
	
	/**
	 * 异步执行网络请求
	 * @param context
	 * @param easyRequestListener
	 */
	public <T> void executeAsync(Context context,EasyRequestVolleyListener<T> easyRequestListener) {
		// TODO Auto-generated method stub
		mEasyRequestListener = easyRequestListener;
		RequestQueue requestQueue = getRequestQueue(context);
		requestQueue.add(this);
		if(mEasyRequestListener != null){
			mEasyRequestListener.onPre(getUrl());
		}
	}
	
	/**
	 *　读取RequestQueue
	 * @param context
	 * @return
	 */
	private RequestQueue getRequestQueue(Context context){
		if(mRequestQueue == null){
			mRequestQueue = Volley.newRequestQueue(context);
		}
		return mRequestQueue;
	}

}
