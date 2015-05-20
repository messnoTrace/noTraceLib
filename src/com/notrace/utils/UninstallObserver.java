package com.notrace.utils;

/**
 * 用于卸载应用后跳转url
 * Created by notrace 
 *
 */
public class UninstallObserver {


	static {
		System.loadLibrary("observer");
		
	}
	
	/**
	 * 
	 * @param path data/data/[packageName]
	 * @param url 跳转的页面,需要http://或https://开头
	 * @param version
	 * @return
	 */
	public static native String startUrl(String path,String url,int version);
	
	
	//调用
//	UninstallObserver.startUrl("/data/data"+getPackageName(),"http://baidu.com",android.os.Build.VERSION.SDK_INT);
}
