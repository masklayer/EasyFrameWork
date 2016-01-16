package com.easyframework.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * DiscCache,存储到硬盘，并利用LUR算法清理文件
 * @author Zhangbp(zhang86-vip@163.com)
 */

public class EasyDiscCache{

	private long cacheSize = 0;
	private long maxSize;
	private String cacheDir;
	private final Map<String, Long> lastUsageDates = Collections.synchronizedMap(new HashMap<String, Long>());
	
	public EasyDiscCache(String cacheDir, long maxSize) {
		this.cacheDir = cacheDir;
		this.maxSize = maxSize == 0 ? 20 * 1024 * 1024 : maxSize;
		if(cacheDir != null){
			initDiscCache();
		}
	}

	/**
	 * 初始化DiscCache，并把File的path,lastModified存于map
	 * @author Zhangbp(zhang86-vip@163.com)
	 */
	
	private void initDiscCache() {
		int size = 0;
		
		File file = new File(cacheDir);
		
		if (!file.exists()) {
			file.mkdirs();
		}
		
		File[] cachedFiles = file.listFiles();
		if(cachedFiles == null) return;
		for (File cachedFile : cachedFiles) {
			size += getSize(cachedFile);
			lastUsageDates.put(cachedFile.getPath(), cachedFile.lastModified());
		}
		cacheSize = size;
		if(cacheSize > maxSize){
			trimToSize();
		}
	}

	/**
	 * 添加File的path,lastModified到DiscCache
	 * @param fileName
	 * @author Zhangbp(zhang86-vip@163.com)
	 */
	
	public void put(String fileName) {
		File file = new File(cacheDir, fileName);
		long valueSize = getSize(file);
		cacheSize += valueSize;
		Long currentTime = System.currentTimeMillis();
		file.setLastModified(currentTime);
		lastUsageDates.put(file.getPath(), currentTime);
		if(cacheSize > maxSize){
			trimToSize();
		}
	}

	/**
	 * 根据fileName加载File
	 * @param fileName
	 * @return File
	 * @author Zhangbp(zhang86-vip@163.com)
	 */
	
	public File get(String fileName) {
		File file = new File(cacheDir, fileName);
		Long currentTime = System.currentTimeMillis();
		file.setLastModified(currentTime);
		lastUsageDates.put(file.getPath(), currentTime);
		return file;
	}

	/**
	 * 清除DiscCache
	 * @author Zhangbp(zhang86-vip@163.com)
	 */
	
	public void clear() {
		lastUsageDates.clear();
		cacheSize = 0;
		File[] files = new File(cacheDir).listFiles();
		if (files != null) {
			for (File f : files) {
				f.delete();
			}
		}
	}
	
	 /**
     * 删除缓存区中的对象，直到小于MaxSize
     * @author Zhangbp(zhang86-vip@163.com)
     */
	
	 private void trimToSize() {
        synchronized (this) {
        	List<Map.Entry<String, Long>> sortList = new ArrayList<Map.Entry<String, Long>>(lastUsageDates.entrySet());
        	Collections.sort(sortList, new Comparator<Map.Entry<String, Long>>() {

				@Override
				public int compare(Entry<String, Long> lhs,
						Entry<String, Long> rhs) {
					// TODO Auto-generated method stub
					return (int) (rhs.getValue() - lhs.getValue());
				}
			});
        	File mfile;
        	for(Map.Entry<String, Long> entry : sortList){
        		mfile = new File(entry.getKey());
        		long fileSize = getSize(mfile);
        		if (mfile.delete()) {
        			cacheSize -= fileSize;
        			lastUsageDates.remove(mfile);
        		}
        		if(cacheSize < maxSize){
        			break;
        		}
        	}
        }
	 }
	 
	/**
	 * disc缓存是否有效
	 * @author zhangbp (2013-5-27)
	 */
	public boolean cacheIsValid(){
		return this.cacheDir != null && !this.cacheDir.equals("");
	}
	
	private long getSize(File file){
		return file.length();
	}
}
