package com.easyframework.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * RequestParameters
 * 
 * @author zhangbp zhang86-vip@163.com
 */

public class EasyRequestParameters implements Parcelable {

	private HashMap<String, String> mParameters;

	public EasyRequestParameters() {
		mParameters = new LinkedHashMap<String, String>();
	}

	public EasyRequestParameters put(String key, String value) {
		this.mParameters.put(key, value);
		return this;
	}

	public EasyRequestParameters put(String value) {
		this.mParameters.put("no-key", value);
		return this;
	}

	public EasyRequestParameters remove(String key) {
		this.mParameters.remove(key);
		return this;
	}

	public String getValue(String key) {
		String rlt = this.mParameters.get(key);
		return rlt;
	}

	public EasyRequestParameters putAll(EasyRequestParameters parameters) {
		this.mParameters.putAll(parameters.mParameters);
		return this;
	}

	public HashMap<String, String> getRequestParameters() {
		return mParameters;
	}

	public byte[] getRquestParam(String paramsEncoding) {

		StringBuilder encodedParams = new StringBuilder();
		String key = "", value = "";
		try {
			int index = 0;
			for (Map.Entry<String, String> entry : mParameters.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();

				if (key == null || value == null)
					continue;

				if (key.equals("no-key")) {
					encodedParams = new StringBuilder();
					encodedParams.append(value);
					break;
				} else {
					if (index != 0) {
						encodedParams.append('&');
					}
					encodedParams.append(URLEncoder.encode(key, paramsEncoding));
					encodedParams.append('=');
					encodedParams.append(URLEncoder.encode(value, paramsEncoding));
				}
				index++;
			}
			return encodedParams.toString().getBytes(paramsEncoding);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
		}
	}

	public byte[] getRquestParam() {
		return getRquestParam("utf-8");
	}

	public void clear() {
		this.mParameters.clear();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeMap(mParameters);

	}

	public static final Parcelable.Creator<EasyRequestParameters> CREATOR = new Parcelable.Creator<EasyRequestParameters>() {

		@SuppressWarnings("unchecked")
		@Override
		public EasyRequestParameters createFromParcel(Parcel source) {
			EasyRequestParameters mEasyRequestParameters = new EasyRequestParameters();
			mEasyRequestParameters.mParameters = source.readHashMap(HashMap.class.getClassLoader());
			return mEasyRequestParameters;
		}

		@Override
		public EasyRequestParameters[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};

}
