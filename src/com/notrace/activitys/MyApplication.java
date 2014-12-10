package com.notrace.activitys;

import com.notrace.utils.CrashHandlerUtil;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	private Context appContext;

	@Override
	public void onCreate() {
		super.onCreate();

		this.appContext = getApplicationContext();
		CrashHandlerUtil handler = CrashHandlerUtil.getInstance();
		handler.init(appContext);
	}

}
