package com.notrace.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.notrace.R;
import com.notrace.utils.AnimotionUtils;
import com.notrace.utils.AppManager;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
//		AppManager.addActivity(this);

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
