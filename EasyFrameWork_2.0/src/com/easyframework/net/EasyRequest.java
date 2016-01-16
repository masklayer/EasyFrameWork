package com.easyframework.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import com.easyframework.util.EasyExecutorService;

import android.os.Handler;
import android.os.Looper;

/**
 * EasyRequest
 * 
 * @author zhangbp
 * 
 */

public class EasyRequest {
	private String mHttpUrl;
	private String mMethod = EasyNetEnum.Method_GET.name();
	private String contentType;
	private HashMap<String, String> mHeader;
	private EasyRequestParameters mEasyRequestParameters;
	private List<EasyFilePart> fileList;
	private static String HEADER_CONTENT_TYPE = "Content-Type";
	private static EasyExecutorService defaultExecutorService = new EasyExecutorService(3, 10);
	private HttpUriRequest mHttpUriRequest;
	
	public EasyRequest(String httpUrl) {
		this.mHttpUrl = httpUrl;
	}

	/**
	 * 设置参数
	 * @param mEasyRequestParameters
	 * @return
	 */
	public EasyRequest setParameters(EasyRequestParameters mEasyRequestParameters) {
		this.mEasyRequestParameters = mEasyRequestParameters;
		return this;
	}
	
	/**
	 * 添加参数
	 * @param mEasyRequestParameters
	 * @return
	 */
	
	public EasyRequest addParameters(EasyRequestParameters mEasyRequestParameters) {
		this.mEasyRequestParameters = this.mEasyRequestParameters == null ? new EasyRequestParameters() : this.mEasyRequestParameters;
		this.mEasyRequestParameters.putAll(mEasyRequestParameters);
		return this;
	}
	
	/**
	 * 添加参数
	 * @param mEasyRequestParameters
	 * @return
	 */
	
	public EasyRequest addParameters(String key,String value) {
		this.mEasyRequestParameters = this.mEasyRequestParameters == null ? new EasyRequestParameters() : this.mEasyRequestParameters;
		this.mEasyRequestParameters.put(key,value);
		return this;
	}
	

	/**
	 * 设置请求的方法
	 * @param mEasyNetEnum
	 * @return
	 */
	public EasyRequest setMethod(EasyNetEnum mEasyNetEnum) {
		this.mMethod = mEasyNetEnum.name();
		return this;
	}

	/**
	 * 添加上传的文件，请用post方法
	 * 
	 * @param mEasyFilePart
	 * @return
	 */
	public EasyRequest addFile(EasyFilePart mEasyFilePart) {
		if (fileList == null) {
			fileList = new ArrayList<EasyFilePart>();
		}
		fileList.add(mEasyFilePart);
		return this;
	}

	/**
	 * 设置request的类型
	 * 
	 * @param contentType
	 * @return
	 */
	public EasyRequest setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	/**
	 * 设置request的header
	 * 
	 * @param mHeader
	 * @return
	 */
	public EasyRequest setHeader(HashMap<String, String> mHeader) {
		this.mHeader = mHeader;
		return this;
	}
	
	/**
	 * 添加request的header
	 * @param mHeader
	 * @return
	 */
	public EasyRequest addHeader(String key,String value) {
		mHeader = mHeader == null ? new HashMap<String, String>() : mHeader;
		mHeader.put(key, value);
		return this;
	}
	
	/**
	 * 添加request的header
	 * @param mHeader
	 * @return
	 */
	public EasyRequest addHeader(HashMap<String, String> mHeader) {
		this.mHeader = this.mHeader == null ? new HashMap<String, String>() : this.mHeader;
		this.mHeader.putAll(mHeader);
		return this;
	}
	
	

	/**
	 * 返回请求的Url
	 * 
	 * @return
	 */
	public String getHttpUrl() {
		return this.mHttpUrl;
	}

	/**
	 * 返回request的方法
	 * 
	 * @return
	 */
	public String getMethod() {
		return this.mMethod;
	}

	/**
	 * 返回header
	 * 
	 * @return
	 */
	public HashMap<String, String> getHeader() {
		return this.mHeader;
	}

	/**
	 * 返回请求参数
	 * 
	 * @return
	 */
	public EasyRequestParameters getParameter() {
		return this.mEasyRequestParameters;
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
	 * 读取request的类型
	 * 
	 * @return
	 */
	public String getContentType() {
		if (contentType == null) {
			contentType = getUploadFiles() == null ? "application/x-www-form-urlencoded; charset=UTF-8" : "multipart/form-data;boundary=" + EasyFormEntity.BOUNDARY;
		}
		return this.contentType;
	}
	
	/**
	 * 中断链接
	 */
	
	public void abort(){
		if(mHttpUriRequest != null){
			mHttpUriRequest.abort();
		}
	}

	/**
	 * 创建HttpUriRequest(HttpGet,HttpPost,HttpDelete,HttpPut)
	 * 
	 * @param request
	 * @return
	 */
	private HttpUriRequest createHttpUriRequest() {
		HttpUriRequest mHttpUriRequest = null;
		if (mMethod.equals(EasyNetEnum.Method_GET.name())) {
			mHttpUriRequest = new HttpGet(diyEasyRequestParam(mHttpUrl));
		} else if (mMethod.equals(EasyNetEnum.Method_POST.name())) {
			mHttpUriRequest = new HttpPost(mHttpUrl);
			diyRequestParam(mHttpUriRequest);
		} else if (mMethod.equals(EasyNetEnum.Method_DELETE.name())) {
			mHttpUriRequest = new HttpDelete(diyEasyRequestParam(mHttpUrl));
		} else if (mMethod.equals(EasyNetEnum.Method_PUT.name())) {
			mHttpUriRequest = new HttpPut(mHttpUrl);
			diyRequestParam(mHttpUriRequest);
		} else {
			throw new IllegalStateException("Unknown request method.");
		}

		if (mHttpUriRequest != null) {
			mHttpUriRequest.addHeader(HEADER_CONTENT_TYPE, getContentType());
			HashMap<String, String> mHeader = getHeader();
			if (mHeader != null) {
				for (String key : mHeader.keySet()) {
					mHttpUriRequest.setHeader(key, mHeader.get(key));
				}
			}
		}

		return mHttpUriRequest;
	}

	/**
	 * 组装简单的请求参数适合(HttpGet | HttpDelete)
	 */
	private String diyEasyRequestParam(String httpUrl) {
		EasyRequestParameters easyRequestParameters = getParameter();
		if (easyRequestParameters != null) {
			String params = new String(easyRequestParameters.getRquestParam());
			if (params != null && !params.equals("")) {
				httpUrl = httpUrl.indexOf("?") != -1 ? httpUrl + "&" + params : httpUrl + "?" + params;
			}
		}
		return httpUrl;
	}

	/**
	 * httpPost | httpPut 组合httpRequest的body
	 * 
	 * @param httpRequest
	 * @param request
	 */
	private void diyRequestParam(HttpUriRequest mHttpUriRequest) {
		// TODO Auto-generated method stub
		HttpEntity mHttpEntity = null;
		EasyRequestParameters easyRequestParameters = getParameter();
		List<EasyFilePart> fileList = getUploadFiles();
		if (fileList != null && fileList.size() > 0) {
			mHttpEntity = new EasyFormEntity(easyRequestParameters, fileList).createByteArrayEntity();
		} else {
			try {
				if (easyRequestParameters != null) {
					mHttpEntity = new StringEntity(new String(easyRequestParameters.getRquestParam()));
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (mHttpEntity != null) {
			((HttpEntityEnclosingRequestBase) mHttpUriRequest).setEntity(mHttpEntity);
		}
	}

	/**
	 * 执行网络请求
	 * 
	 * @return
	 */
	public HttpResponse execute() {
		return execute(null);
	}
	
	/**
	 * 执行网络请求
	 * 
	 * @param mHttpClient
	 * @return
	 */
	public HttpResponse execute(HttpClient mHttpClient) {
		mHttpClient = mHttpClient == null ? EasyHttpClient.getInstance().getHttpClient() : mHttpClient;
		mHttpUriRequest = createHttpUriRequest();
		
		if (mHttpUriRequest != null) {
			try {
				return mHttpClient.execute(mHttpUriRequest);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 异步执行网络请求
	 * @param <T>
	 * 
	 * @param mEasyRequestListener
	 */

	public <T> void executeAsync(final EasyRequestListener<T> mEasyRequestListener) {
		executeAsync(mEasyRequestListener, null, null);
	}

	/**
	 * 异步执行网络请求
	 * @param <T>
	 * 
	 * @param mEasyRequestListener
	 * @param mHttpClient
	 * @param mEasyExecutorService
	 */
	public <T> void executeAsync(final EasyRequestListener<T> mEasyRequestListener, final HttpClient mHttpClient, EasyExecutorService mEasyExecutorService) {

		if (mEasyRequestListener != null) {
			mEasyRequestListener.onPre(mHttpUrl);
		}

		mEasyExecutorService = mEasyExecutorService == null ? defaultExecutorService : mEasyExecutorService;

		mEasyExecutorService.submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpResponse mHttpResponse = execute(mHttpClient);
				if (mEasyRequestListener != null) {
					final T data = mEasyRequestListener.onProcess(mHttpUrl, mHttpResponse);
					new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mEasyRequestListener.onComplete(mHttpUrl, data);
						}
					}, 0);
				}
				abort();
			}
		});
	}

}
