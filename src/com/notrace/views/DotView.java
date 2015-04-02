package com.notrace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DotView extends View {
	private Paint mPaint;

	private int y = 960;
	private int x = 560;
	private int tep = 1;

	public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public DotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DotView(Context context) {
		super(context);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
	
		

		canvas.drawColor(Color.WHITE);

		for (int i = 0; i < 20; i++) {
			mPaint.setAlpha(100 - i * 4);
			canvas.drawCircle(x, y - 48 * i, 3, mPaint);
		}

		mPaint.setAlpha(100);
		canvas.drawCircle(x, y - 48 * (tep - 1), 4, mPaint);
		canvas.drawCircle(x, y - 48 * (tep), 4, mPaint);
		canvas.drawCircle(x, y - 48 * (tep + 1), 4, mPaint);
		super.onDraw(canvas);
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(3);
		mPaint.setStyle(Paint.Style.FILL);
	}

	public void reDraw(int tep) {
		
			this.tep = tep%20;
		invalidate();
	}

}
