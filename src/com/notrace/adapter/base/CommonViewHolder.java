package com.notrace.adapter.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommonViewHolder {
	private final SparseArray<View> mViews;
	private View mConvertView;
	
	private CommonViewHolder(Context context,ViewGroup parent ,int layoutId,int position){
		this.mViews=new SparseArray<View>();
		mConvertView=LayoutInflater.from(context).inflate(layoutId, parent,false);
		
		mConvertView.setTag(this);
		
	}
	
	/**
	 * 
	* @Title get
	* @Description 拿到一个ViewHolder对象 
	* @param context
	* @param convertView
	* @param parent
	* @param layoutId
	* @param position
	* @return 
	* @Return CommonViewHolder 返回类型
	* @Throws
	 */
	public static CommonViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
		if(convertView==null){
			return new CommonViewHolder(context, parent, layoutId, position);
		}
		return (CommonViewHolder) convertView.getTag();
	}
	
	
	public <T extends View>T getView(int viewId){
		View view=mViews.get(viewId);
		if(view==null){
			view=mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T)view;
	}
	
	public View getConvertView()
	{
		return mConvertView;
	}
	
	
}
