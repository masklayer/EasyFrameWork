/**
 * 
 */
package com.easyframework.net;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.ByteArrayEntity;

/**
 * @author zhangbp
 * 
 */
public class EasyFormEntity {

	public final static String BOUNDARY = "7d62bf2f9066c";
	private final String MP_BOUNDARY = "--" + BOUNDARY;
	private final String END_MP_BOUNDARY = "--" + BOUNDARY + "--";
	protected static final String CONTENT_DISPOSITION = "Content-Disposition: form-data; name=";

	private EasyRequestParameters mEasyRequestParameters;
	private List<EasyFilePart> fileList;

	public EasyFormEntity(EasyRequestParameters mEasyRequestParameters, List<EasyFilePart> fileList) {
		this.mEasyRequestParameters = mEasyRequestParameters;
		this.fileList = fileList;
	}

	public byte[] getByte() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (mEasyRequestParameters != null) {
			createParameter(out, mEasyRequestParameters);
		}

		if (fileList != null) {
			createFile(out, fileList);
		}

		copyIo(out, ("\r\n" + END_MP_BOUNDARY).getBytes());

		byte[] finalByte = out.toByteArray();

		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalByte;
	}

	public ByteArrayEntity createByteArrayEntity() {
		return new ByteArrayEntity(getByte());
	}

	private void copyIo(ByteArrayOutputStream out, byte[] b) {
		try {
			out.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 拼装http协议格式的 字节
	 * 
	 * @Param RequestParameters
	 * @author zhangbp
	 * @return byte[]
	 */

	private void createParameter(ByteArrayOutputStream out, EasyRequestParameters mRequestParameters) {
		HashMap<String, String> mParameters = mRequestParameters.getRequestParameters();
		StringBuffer sb = new StringBuffer();
		
		for(String key : mParameters.keySet()){
			sb.append(MP_BOUNDARY).append("\r\n");
			sb.append("content-disposition: form-data; name=\"").append(key).append("\"\r\n\r\n");
			sb.append(mParameters.get(key)).append("\r\n");
		}
		
		byte[] paramByte = sb.toString().getBytes();
		if (paramByte != null && paramByte.length > 0) {
			copyIo(out, paramByte);
		}
	}

	/**
	 * 文件按http格式封装到ByteArrayOutputStream
	 * @param out
	 * @param fileList
	 */
	private void createFile(ByteArrayOutputStream out,List<EasyFilePart> fileList) {
		
		for (EasyFilePart fileItem : fileList) {
			
			StringBuilder headerStr = new StringBuilder();
			headerStr.append(MP_BOUNDARY).append("\r\n");
			headerStr.append("Content-Disposition: form-data; name=\"" + fileItem.name + "\"; filename=\""+fileItem.file.getName()+"\"\r\n");
			String filetype = fileItem.contentType == null || fileItem.contentType.equals("") ? "application/octet-stream" : fileItem.contentType;
			headerStr.append("Content-Type: "+filetype+"\r\n\r\n");
			byte[] headerByte = headerStr.toString().getBytes();
			FileInputStream input = null;
			try {
				out.write(headerByte);
				input = new FileInputStream(fileItem.file);
				byte[] buffer = new byte[1024 * 50];
				while (true) {
					int count = input.read(buffer);
					if (count == -1) {
						break;
					}
					out.write(buffer, 0, count);
				}
				out.write("\r\n".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
