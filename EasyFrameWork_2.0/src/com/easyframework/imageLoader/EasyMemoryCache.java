
package com.easyframework.imageLoader;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MemoryCache,用LRU算法对象缓存到内存
 * @author Zhangbp(zhang86-vip@163.com)
 */

public abstract class EasyMemoryCache<K, V> {
    private final LinkedHashMap<K, V> map = new LinkedHashMap<K, V>(0, 0.75f, true);
    private int cacheSize;
    private int maxSize;
    public EasyMemoryCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
    }
    
    /**
     * key,从缓存取出对象
     * @param key
     * @author Zhangbp(zhang86-vip@163.com)
     */
    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V mapValue = null;
        synchronized (this) {
            mapValue = map.get(key);
        }
        return mapValue;
    }
    
    /**
     * 添加对象到缓存区
     * @param key
     * @param value
     * @author Zhangbp(zhang86-vip@163.com)
     */
    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        V previous;
        synchronized (this) {
        	cacheSize += safeSizeOf(key, value);
            previous = map.put(key, value);
            if (previous != null) {
            	cacheSize -= safeSizeOf(key, previous);
            }
        }
        
        if(cacheSize >= maxSize){
        	trimToSize();
        }
        return previous;
    }

    /**
     * 删除缓存区中的对象
     * @param key
     * @author Zhangbp(zhang86-vip@163.com)
     */
    public final void remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
        	map.remove(key);
        	deleteObject(key, get(key));
        }
    }
    
    
    /**
     * 删除缓存区中的对象，直到小于MaxSize
     * @author Zhangbp(zhang86-vip@163.com)
     */
    
    private void trimToSize() {
        while (true) {
            K key;
            V value;
            synchronized (this) {
                if (cacheSize < 0 || (map.isEmpty() && cacheSize != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }

                if (cacheSize <= maxSize) {
                    break;
                }
                
                Map.Entry<K, V> toEvict =  (Map.Entry<K, V>)this.map.entrySet().iterator().next();
                if (toEvict == null) {
                    break;
                }
                key = toEvict.getKey();
                value = toEvict.getValue();
                remove(key);
                cacheSize -= safeSizeOf(key, value);
            }
        }
    }


    
    /**
     * 清除缓存区
     * @author Zhangbp(zhang86-vip@163.com)
     */
    
    public final void clear() {
    	map.clear();
    }
    

    /**
     * 计算对象的大小
     * @param key
     * @param value
     * @return
     * @author zhangbp (2013-5-16)
     */
    private int safeSizeOf(K key, V value) {
        int result = sizeOf(key, value);
        if (result < 0) {
            throw new IllegalStateException("Negative size: " + key + "=" + value);
        }
        return result;
    }

    
    public abstract int sizeOf(K key, V value);
    
    /**
     * 删除缓存的对象，一个要重写这个方法做相应的工作
     * @param key
     * @param value
     * @return true-清除缓存区的引用,false--不操作
     * @author zhangbp (2013-5-16)
     */
    public void deleteObject(K key, V value){
    	
    }
    
    public synchronized final int getCacheSize() {
        return cacheSize;
    }

    public synchronized final int getMaxSize() {
        return maxSize;
    }

}
