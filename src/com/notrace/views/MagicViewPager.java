package com.notrace.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015-03-19.
 */

@SuppressLint("NewApi")
public class MagicViewPager extends ViewPager {
    public MagicViewPager(Context context) {
        super(context);
        init();
    }

    public MagicViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init()
    {
        setPageTransformer(true,new ViewPager.PageTransformer(){

            @Override
            public void transformPage(View paramAnonymousView, float paramAnonymousFloat) {
                if ((paramAnonymousFloat < 0.0F) || (paramAnonymousFloat >= 1.0F)) {
                    paramAnonymousView.setTranslationX(0.0F);
                    paramAnonymousView.setAlpha(1.0F);
                    paramAnonymousView.setScaleX(1.0F);
                    paramAnonymousView.setScaleY(1.0F);
                    return;
                }
                paramAnonymousView.setTranslationX(-paramAnonymousFloat * paramAnonymousView.getWidth());
                paramAnonymousView.setAlpha(Math.max(0.0F, 1.0F - paramAnonymousFloat));
                float f = Math.max(0.0F, 1.0F - 0.3F * paramAnonymousFloat);
                paramAnonymousView.setScaleX(f);
                paramAnonymousView.setScaleY(f);
            }
        });
    }
}
