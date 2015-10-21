package com.notrace.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class FinalScrollView extends ScrollView {

	private OnScrollChangedListener scrollChangedListener;

	public FinalScrollView(Context context) {
		super(context);
	}

	public FinalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FinalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnScrollChangedListener(OnScrollChangedListener l) {
		this.scrollChangedListener = l;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (scrollChangedListener != null) {
			scrollChangedListener.onScrollChanged(l, t, oldl, oldt);
			if (isScrollToBottom()) {
				scrollChangedListener.onScrollBottom();
			}
		}
	}

	protected boolean isScrollToBottom() {
		if (getScrollY() + getHeight() >= computeVerticalScrollRange()) {
			return true;
		} else {
			return false;
		}
	}

	
	public interface OnScrollChangedListener {
		public void onScrollBottom();
		public void onScrollChanged(int left, int top, int oldLeft, int oldTop);
	}

}
