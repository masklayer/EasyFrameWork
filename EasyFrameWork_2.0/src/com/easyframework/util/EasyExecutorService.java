

package com.easyframework.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * @author zhangbp
 *
 */
public class EasyExecutorService {

	private ExecutorService mExecutorService;
	private int threadPoolSize;
	private int maxThreadPoolSize;

	/**
	 * @param threadPoolSize 保持的线程线
	 * @param maxThreadPoolSize 最大的线程线
 	 */
	public EasyExecutorService(int threadPoolSize, int maxThreadPoolSize){
		this.threadPoolSize = threadPoolSize;
		this.maxThreadPoolSize = maxThreadPoolSize;
	}
	
	/**
	 * 初始化线程
	 * @author zhangbp (2013-5-21)
	 */
	
	private void initExecutors() {
		if (mExecutorService == null || mExecutorService.isShutdown()) {
			mExecutorService = createTaskExecutor();
		}
	}

	/**
	 * 创建线程池
	 * @return ExecutorService
	 * @author zhangbp (2013-5-21)
	 */
	private ExecutorService createTaskExecutor() {
		return new ThreadPoolExecutor(threadPoolSize,maxThreadPoolSize, 0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
	}
	
	/**
	 * 提交任务
	 * @param task
	 * @author zhangbp (2013-5-23)
	 */
	public Future<?> submit(Runnable task) {
		initExecutors();
		return mExecutorService.submit(task);
	}


	public void stop() {
		if (mExecutorService != null) {
			mExecutorService.shutdownNow();
		}
	}
	
	
}
