package com.notrace.activitys;

import java.io.File;

import android.R.anim;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.notrace.R;

public class MyApplication extends Application {

	private Context appContext;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext=getApplicationContext();
		initImageLoader();
//		this.appContext = getApplicationContext();
//		CrashHandlerUtil handler = CrashHandlerUtil.getInstance();
//		handler.init(appContext);
	}

	
	private void initImageLoader() {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(android.R.color.transparent)
				.showImageForEmptyUri(null).showImageOnFail(null)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();

//		File cacheDir = new File(SDCardUtil.getCachDirFolder());
//		if (!cacheDir.exists()) {
//			cacheDir.mkdirs();
//		}
		File cacheDir = new File("cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				appContext).threadPriority(Thread.NORM_PRIORITY - 2)
		/* .denyCacheImageMultipleSizesInMemory() */.threadPoolSize(3)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
	}
	
}
