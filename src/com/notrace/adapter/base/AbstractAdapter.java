package com.notrace.adapter.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * A abstract class always use on BaseAdapter to reduce code about bind view and
 * ViewHolder
 * 
 * @author noTrace
 * 
 * @param <T>
 */
public abstract class AbstractAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected String TAG = this.getClass().getName();
	protected LayoutInflater inflater;
	private List<T> list;

	public AbstractAdapter(Context context) {
		this(context, null);
	}

	public AbstractAdapter(Context context, List<T> list) {
		mContext = context;
		if (list == null) {
			this.list = new ArrayList<T>();
		} else {
			this.list = list;
		}
		inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public T getItem(int position) {
		if(list==null||list.size()==0||position>=list.size()){
			return null;
		}
		return list.get(position);
	}

	
	@Override
	public int getItemViewType(int position) {
		//ovveride if is multi type layout
		return super.getItemViewType(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		int type=getItemViewType(position);
		
		
		View currentFocus=((Activity)mContext).getCurrentFocus();
		if(currentFocus !=null){
			currentFocus.clearFocus();
		}
		BaseViewHolder<T> holder;
		if(convertView ==null){
			convertView=View.inflate(mContext, getItemViewLayout(type), null);
			holder=getItemViewHolder(type);
			 ViewInjectorByReflect.injectView(holder, convertView);
			 holder.setFixedData();
	            convertView.setTag(holder);
	        } else {
	            holder = (BaseViewHolder<T>) convertView.getTag();
	        }
	        T data = getItem(position);

	        holder.setData(data, position);
	        return convertView;
	}
	
	
	/**
	 * get the item layout base on viewType
	 * 
	 * @param itemViewType
	 * @return
	 */
	protected abstract int getItemViewLayout(int itemViewType);

	protected abstract BaseViewHolder<T> getItemViewHolder(int itemViewType);
	
	public abstract class BaseViewHolder<D> implements UnMixable{
		public abstract void setData(D data,int posotion);
		
		public void setFixedData(){}
	}
	
	protected Context getContext(){
		return mContext;
	}
	
	
	/**
	 * 统一去通知数据集已更新
	 */
	public synchronized void syncNotifyDataSetChanged(){
		notifyDataSetChanged();
	}
}
