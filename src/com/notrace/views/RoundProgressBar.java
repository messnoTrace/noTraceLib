package com.notrace.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.notrace.R;

/**
 * Created by noTrace on 2014/12/4.
 * 带进度的圆形的进度条，线程安全的view，可直接在线程中更新进度
 */
public class RoundProgressBar extends View {

    /**
     * 画笔对象的引用
     */
    private Paint paint;
    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgreeColor;

    /**
     * 中间进度百分比的字符的颜色
     */

    private int textColor;

    /**
     * 中间进度百分比的字体大小
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;

    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度条的风格，实心或者空心
     */
    private int style;
    private static final int STROKE=0;
    private static final int FILL=1;

    public RoundProgressBar(Context context) {
        super(context);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint=new Paint();
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.RoundProgressBar);

        roundColor=ta.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgreeColor=ta.getColor(R.styleable.RoundProgressBar_roundProgressColor,Color.GREEN);
        textColor=ta.getColor(R.styleable.RoundProgressBar_textColor,Color.GREEN);

        textSize=ta.getDimension(R.styleable.RoundProgressBar_textSize,15);
        roundWidth=ta.getDimension(R.styleable.RoundProgressBar_roundWidth ,5);

        max=ta.getInteger(R.styleable.RoundProgressBar_max,100);
        textIsDisplayable =ta.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable,true);
        style=ta.getInt(R.styleable.RoundProgressBar_style,0);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外面的圆环
         */

        int center=getWidth()/2;//获取圆心的x坐标
        int radius=(int )(center-roundWidth/2);// 圆环的半径
        paint.setColor(roundColor);//设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE);//设置空心
        paint.setStrokeWidth(roundWidth);//设置圆环的宽度
        paint.setAntiAlias(true);//  消除锯齿
        canvas.drawCircle(center,center,radius,paint);

        /**
         * 画进度百分比
         */

        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        //
        Paint.FontMetrics fontMetrics=paint.getFontMetrics();
        //计算每个坐标
        float baseX=center;
        float fontTotalHeight=fontMetrics.bottom-fontMetrics.top;
        float offY=fontTotalHeight/2-fontMetrics.bottom;
        float newY=center+ offY;

        paint.setTextAlign(Paint.Align.CENTER);
        int percent = (int) (((float) progress / (float) max) * 100); // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent + "%"); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        String text = percent + "%";
        if (textIsDisplayable && percent != 0 && style == STROKE) {
            // 此处需要修改
            // canvas.drawText(percent + "%", centre - textWidth / 2, centre
            // + textSize / 2, paint); // 画出进度百分比
            canvas.drawText(text, baseX, newY, paint);
        }

/**
 * 画圆弧 ，画圆环的进度
 */

        // 设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setColor(roundProgreeColor); // 设置进度的颜色
        RectF oval = new RectF(center - radius, center - radius, center
                + radius, center + radius); // 用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -90, 360f * progress / max, false, paint); // 根据进度画圆弧
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // 根据进度画圆弧
                break;
            }
        }


    }


    /**
     * 获取进度最大值
     * @return
     */
    public synchronized int getMax(){
        return max;
    }

    /**
     * 设置进度最大值
     * @param max
     */
    public synchronized  void setMax(int max){
        if(max <0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max=max;
    }

    /**
     * 获取进度
     * @return
     */
    public synchronized int getProgress(){
        return  progress;
    }
    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgree(int progress){
        if(progress <0){
            throw new IllegalArgumentException(" progress not less  0 ");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgreeColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgreeColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }




}
