package com.notrace.activitys;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.notrace.R;
import com.notrace.immersivestatusbar.SystemBarTintManager;
import com.notrace.utils.AnimotionUtils;
import com.notrace.utils.AppManager;

public abstract class BaseActivity extends FragmentActivity {

	
	
	//沉浸式菜单栏
	protected SystemBarTintManager mTintManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		//设置App的主体颜色
//		mTintManager.setStatusBarTintResource(R.color.navigation_bg);
		
		
//		AppManager.addActivity(this);

	}
	
	@TargetApi(19)
	protected void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		AnimotionUtils.pushFromRight(this);
	}

	@Override
	public void finish() {
		super.finish();
		AnimotionUtils.outFromLeft(this);
//		AppManager.removeActivity(this);
	}
	
	//set layout
	public abstract void findViews();

	
	//bind listener
	public abstract void bindListener();
	
	//init compounent
	public abstract void initView();
	
	public abstract void initData();
}
