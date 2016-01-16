package com.easyframework.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Many streams obtained over slow connection show <a href="http://code.google.com/p/android/issues/detail?id=6066">this
 * problem</a>.
 * 官方提供解决 Bitmapfactory.decodestream在网络环境慢，解码失败的问题
 */
public class FlushedInputStream extends BufferedInputStream {

	public FlushedInputStream(InputStream inputStream) {
		super(inputStream);
	}
	
	public FlushedInputStream(InputStream inputStream, int size) {
		super(inputStream, size);
	}

	@Override
	public long skip(long n) throws IOException {
		long totalBytesSkipped = 0L;
		while (totalBytesSkipped < n) {
			long bytesSkipped = in.skip(n - totalBytesSkipped);
			if (bytesSkipped == 0L) {
				int by_te = read();
				if (by_te < 0) {
					break; // we reached EOF
				} else {
					bytesSkipped = 1; // we read one byte
				}
			}
			totalBytesSkipped += bytesSkipped;
		}
		return totalBytesSkipped;
	}
}
