package com.easyframework.net;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * 自定义参数的Httpclient。<br>
 * 提供httpGet，httpPost,HttpDelete,HttpPut传送消息的方式<br>
 * 提供httpPost,HttpPut上传文件的方式
 */

public class EasyHttpClient{


	// TODO SDK默认参数设置
	public static final int CONNECTION_TIMEOUT = 5000;
	public static final int CON_TIME_OUT_MS = 5000;
	public static final int SO_TIME_OUT_MS = 5000;
	public static final int MAX_CONNECTIONS_PER_HOST = 50;
	public static final int MAX_TOTAL_CONNECTIONS = 500;

	private HttpClient httpClient;
	private static EasyHttpClient mEasyHttpClient;

	public static synchronized EasyHttpClient getInstance() {
		if (mEasyHttpClient == null) {
			mEasyHttpClient = new EasyHttpClient();
		}
		return mEasyHttpClient;
	}

	private EasyHttpClient() {
		this(MAX_CONNECTIONS_PER_HOST, MAX_TOTAL_CONNECTIONS, CON_TIME_OUT_MS, SO_TIME_OUT_MS);
	}

	private EasyHttpClient(int maxConnectionPerHost, int maxTotalConnections, int conTimeOutMs, int soTimeOutMs) {

		SchemeRegistry supportedSchemes = new SchemeRegistry();
		supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		HttpParams httpParams = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(httpParams, maxTotalConnections);
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(maxConnectionPerHost);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);

		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUseExpectContinue(httpParams, false);

		ThreadSafeClientConnManager mThreadSafeClientConnManager = new ThreadSafeClientConnManager(httpParams, supportedSchemes);
		HttpConnectionParams.setConnectionTimeout(httpParams, conTimeOutMs);
		HttpConnectionParams.setSoTimeout(httpParams, soTimeOutMs);

		HttpClientParams.setCookiePolicy(httpParams, CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient = new DefaultHttpClient(mThreadSafeClientConnManager, httpParams);
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

}
