package com.easyframework.widget;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * EasyBannerAdapter
 * @author zhangbp
 *
 * @param <T>
 */

public abstract class EasyBannerAdapter<T> extends PagerAdapter{
	
	private ArrayList<T> dataArrayList;
	private View cacheItemView;
	private boolean isLoop = true;
	private boolean isAutoRoll = true;
	
	public EasyBannerAdapter(){
		dataArrayList = new ArrayList<T>();
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return isLoop ? Integer.MAX_VALUE : getDataSize();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		cacheItemView = (View)object;
		container.removeView(cacheItemView);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		position = getFinalPosition(position);
		View itemView = getView(position, cacheItemView, container);
		cacheItemView = null;
		container.addView(itemView);
		return itemView;
	}
	

	public abstract View getView(int position, View convertView, ViewGroup parent);
	
	/**
	 * 读取单条数据
	 * @param position
	 * @return
	 */
	public T getItem(int position){
		return dataArrayList.get(position);
	}
	
	/**
	 * 数据的长度
	 * @return
	 */
	public int getDataSize(){
		return dataArrayList.size();
	}
	
	/**
	 * 批量添加数据
	 * @param datas
	 */
	public void addData(ArrayList<T> datas){
		dataArrayList.addAll(datas);
	}
	
	/**
	 * 添加单条数据
	 * @param mt
	 */
	public void addData(T mt){
		dataArrayList.add(mt);
	}
	
	/**
	 * 设置是否自动滚动,默认true
	 * @param isloop
	 */
	public void isAutoRoll(boolean isAutoRoll){
		this.isAutoRoll = isAutoRoll;
	}
	
	public boolean isAutoRoll(){
		return this.isAutoRoll;
	}
	
	/**
	 * 设置是否无限循环,默认true
	 * @param isloop
	 */
	public void isLoop(boolean isloop){
		this.isLoop = isloop;
	}
	
	public boolean isLoop(){
		return this.isLoop;
	}
	
	public int getFinalPosition(int position){
		return  position % dataArrayList.size();
	}
}
