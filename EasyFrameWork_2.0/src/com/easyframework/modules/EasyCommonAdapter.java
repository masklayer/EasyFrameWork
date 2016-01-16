package com.easyframework.modules;

import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;

/**
 * 自定义通用Adapter
 * @author zhangbp
 * 
 */
public abstract class EasyCommonAdapter<T> extends BaseAdapter{
	public List<T> mListData = new ArrayList<T>();
	
	public EasyCommonAdapter(){
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListData.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/**
	 * 添加数据
	 * @author zhangbp
	 */
	public void appendData(List<T> list){
		mListData.addAll(list);
		notifyDataSetChanged();
	}
	/**
	 * 添加单条数据
	 * @author zhangbp
	 */
	public void appendData(T itemData){
		mListData.add(itemData);
		notifyDataSetChanged();
	}
	/**
	 * 移除单条数据
	 * @author zhangbp
	 */
	public void removeData(int index){
		mListData.remove(index);
		notifyDataSetChanged();
	}
	/**
	 * 清除所有数据
	 * @author zhangbp
	 */
	public void clearData(){
		mListData.clear();
		notifyDataSetChanged();
	}
	
	/**
	 * 替换数据
	 * @author zhangbp
	 */
	
	public void replaceData(List<T> list){
		mListData = list;
		notifyDataSetChanged();
	}
	
	/**
	 * 替换单条数据
	 * @author zhangbp
	 */
	
	public void replaceData(T itemData,int position){
		mListData.set(position, itemData);
		notifyDataSetChanged();
	}
	
	
	/**
	 * 取出所有数据
	 * @author zhangbp
	 */
	public List<T> getAllData(){
		return mListData;
	}
	
	/**
	 * 取出单条数据
	 * @author zhangbp
	 */
	public T getItemData(int location){
		return mListData.get(location);
	}

}
