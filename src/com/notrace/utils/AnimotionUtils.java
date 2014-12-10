package com.notrace.utils;

import android.app.Activity;
import com.notrace.R;

/**
 * Created by noTrace on 2014/11/14.
 *
 */
public class AnimotionUtils {

    /**
     *activity 进入动画
     * @param activity
     */
   public static void pushFromRight(Activity activity){

       activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
   }

    /**
     * activity 退出动画
     * @param activity
     */

    public static void  outFromLeft(Activity activity){
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
}
