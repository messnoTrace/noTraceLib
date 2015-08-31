package com.notrace.fragments;

import android.annotation.SuppressLint;

/**
*http://blog.csdn.net/maosidiaoxian/article/details/38300627
 * 
 */
public abstract class LazyFragment extends BaseFragment{
	protected boolean isVisible;

	/**
	 */
	@SuppressLint("NewApi")
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}

	protected void onVisible() {
		initData();
	}


	protected void onInvisible() {
	};

}
