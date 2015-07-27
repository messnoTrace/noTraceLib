package com.notrace.views;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * 视图闪烁动画效果
 * 
 * */
public class ViewFlicker {
    private ViewFlicker( ){
        
    }
    
    /**
     *View闪烁效果
     * 
     * */
    public static void startFlick( View view ){
        if( null == view ){
            return;
        }
        
        stopFlick( view );
        Animation alphaAnimation = new AlphaAnimation( 1, 0 );
        
        alphaAnimation.setDuration( 500 );
        alphaAnimation.setInterpolator( new LinearInterpolator( ) );
        alphaAnimation.setRepeatCount( Animation.INFINITE );
        alphaAnimation.setRepeatMode( Animation.REVERSE );
        
        view.startAnimation( alphaAnimation );
    }
    
    /**
     * 取消View闪烁效果
     * 
     * */
    public static void stopFlick( View view ){
        if( null == view ){
            return;
        }
        
        view.clearAnimation( );
    }
}
