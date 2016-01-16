package com.easyframework.imageLoader;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;


/**
 * 滚动监听器 ,用来控制什么情况下不加载图片
 * @author zhangbp
 */
public class EasyPauseOnScrollListener implements OnScrollListener {

	private final boolean pauseOnScroll;
	private final boolean pauseOnFling;
	private final OnScrollListener externalListener;

	
	/**
	 * @param pauseOnScroll
	 * @param pauseOnFling
	 */
	public EasyPauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
		this(pauseOnScroll, pauseOnFling, null);
	}

	
	/**
	 * @param pauseOnScroll
	 * @param pauseOnFling
	 * @param customListener
	 */
	public EasyPauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling, OnScrollListener customListener) {
		this.pauseOnScroll = pauseOnScroll;
		this.pauseOnFling = pauseOnFling;
		externalListener = customListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				EasyImageLoader.getInstance().resume();
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				if (pauseOnScroll) {
					EasyImageLoader.getInstance().pause();
				}
				break;
			case OnScrollListener.SCROLL_STATE_FLING:
				if (pauseOnFling) {
					EasyImageLoader.getInstance().pause();
				}
				break;
		}
		if (externalListener != null) {
			externalListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (externalListener != null) {
			externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}
}
