package com.notrace.views;

import com.notrace.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 *  a textview which can rotate some degree
 * @author noTrace
 *
 */
public class RotateView extends TextView {
	private static int DEFAULT_DEGREE = 0;
	private int mDegree;

	public RotateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RotateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.RotateView);
		mDegree = array.getDimensionPixelOffset(R.styleable.RotateView_degree,
				DEFAULT_DEGREE);
		array.recycle();
	}

	public RotateView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(getCompoundPaddingLeft(), getCompoundPaddingTop());
		canvas.rotate(mDegree, this.getWidth() / 2f, this.getHeight() / 2f);
		super.onDraw(canvas);
		canvas.restore();
	}

}
