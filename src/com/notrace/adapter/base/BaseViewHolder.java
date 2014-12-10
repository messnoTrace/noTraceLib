package com.notrace.adapter.base;

/**
 * 数据绑定类， 包含绑定的控件成员，绑定方法
 * @author noTrace
 *
 * @param <D>
 */
public interface BaseViewHolder<D> extends UnMixable {
    /**
     * bind the data message
     *
     * @param data     the data params
     * @param position the position of data in the datasArray
     */
	public abstract<T extends D> void setData(T data,int posotion);
}
