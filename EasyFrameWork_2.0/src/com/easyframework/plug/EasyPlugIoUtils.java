
package com.easyframework.plug;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author zhangbp
 *
 */
public final class EasyPlugIoUtils {

	private static final int BUFFER_SIZE = 8 * 1024; // 8 KB 

	private EasyPlugIoUtils() {
	}

	public static void copyStream(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		while (true) {
			int count = is.read(bytes, 0, BUFFER_SIZE);
			if (count == -1) {
				break;
			}
			os.write(bytes, 0, count);
		}
	}

	public static void closeSilently(Closeable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
			// Do nothing
		}
	}
	
	public static String streamToString(InputStream is)  throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		StringBuffer b = new StringBuffer();
		while (true) {
			int count = is.read(bytes, 0, BUFFER_SIZE);
			if (count == -1) {
				break;
			}
			b.append(new String(bytes, 0, count));
		}
		return b.toString();
	}
	
}
