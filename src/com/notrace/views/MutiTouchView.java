package com.notrace.views;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by noTrace on 2014/11/28.
 */
public class MutiTouchView extends View {
    private static final int EVENT_DRAG=1;
    private static final int EVENT_ZOOM=2;
    private static final int EVENT_ROTATE=3;

    private int eventType=0;

    private GestureDetector mGestureDetector;
    private OnTouchListener mOnTouchListener;

    private boolean canLoadTouchEvent=false;

    public void setCanLoadTouchEvent(boolean loadTouchEvent){
        if(loadTouchEvent){
            this.setClickable(true);
            GestureDetector.OnGestureListener mGestureListener=new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent motionEvent) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent motionEvent) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    if(mOnTouchListener !=null){
                        mOnTouchListener.onSingleTapUp(MutiTouchView.this);
                    }
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
                    if(eventType==EVENT_DRAG){
                        View view=MutiTouchView.this;

                        view.setX(view.getX()+(motionEvent2.getX()-motionEvent.getX())*view.getScaleX());
                        view.setY(view.getY()+(motionEvent2.getY()-motionEvent.getY())* view.getScaleY());
                    }
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent motionEvent) {

                }

                @Override
                public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
                    return false;
                }

            };
            mGestureDetector =new GestureDetector(this.getContext(),mGestureListener);
        }else{
            this.setClickable(false);
            mGestureDetector=null;
        }
    }


    public static interface  OnTouchListener{
        public void  onSingleTapUp(View view);

    }
    public void setOnTouchListener(OnTouchListener listener){
        this.mOnTouchListener=listener;
    }

    private float startDis=0;

    public MutiTouchView(Context context) {
        super(context);
    }

    public MutiTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MutiTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(canLoadTouchEvent){
            mGestureDetector.onTouchEvent(event);
            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    eventType=EVENT_DRAG;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(eventType==EVENT_ZOOM){
                        float dis=getEventAngle(event);

                        if(dis>10f){
                            View view=MutiTouchView.this;
                            float s=dis/startDis;
                            view.setScaleX(view.getScaleX() * s);
                            view.setScaleY(view.getScaleY() * s);
                        }
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    startDis=getEventDistance(event);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    eventType = 0;
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private float getEventDistance(MotionEvent event){
        float dx=event.getX(1)-event.getX(0);
        float dy =event.getY(1)-event.getY(0);
        return (float)Math.sqrt(dx*dx+dy*dy);
    }

    //获得旋转角度
    public float getEventAngle(MotionEvent event){
        double DeltalX = event.getX(0) - event.getX(1);
        double DeltalY = event.getY(0) - event.getY(1);

        return (float) Math.abs(Math.atan2(DeltalX, DeltalY));
    }

    //返回两点的中点
    public PointF getMidPoint(MotionEvent Event)
    {
        float X=Event.getX(0)+Event.getX(1);
        float Y=Event.getY(0)+Event.getY(1);
        return new PointF(X/2,Y/2);
    }

}
