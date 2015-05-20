package com.notrace.activitys;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.R.anim;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.Display;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.notrace.R;

public class MyApplication extends Application {
	
	
	// 运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();

    private HashMap<String, Boolean> mapActivity = new HashMap<String, Boolean>();// 用于存储activity对应的激活状态

    private static Context appContext;
    // 为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static MyApplication application;
    private DisplayMetrics displayMetrics = null;

    //image memory cache
    private LruCache<String, Bitmap> appBitmapCache = null;


    private Handler handler = new Handler();

    // 构造方法
    public synchronized static MyApplication getInstance() {
        return application;
    }
	

	@Override
	public void onCreate() {
		 super.onCreate();
	        if (appContext == null) {
	            appContext = getApplicationContext();
	        }
	        application = this;
		
		initImageLoader();

	}

	 @Override
	    public void onTerminate() {
	        super.onTerminate();
	    }

	    public static Context getApplication() {
	        return appContext;
	    }

	    // add Activity
	    public void addActivity(Activity activity) {
	        mList.add(activity);
	    }

	    // 关闭每一个list内的activity
	    public void exitApp() {
	        try {
	            for (Activity activity : mList) {
	                if (activity != null)
	                    activity.finish();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            System.exit(0);
	        }
	    }

	    // 关闭每一个list内的activity
	    public void closeActivity() {
	        try {
	            for (Activity activity : mList) {
	                if (activity != null) {
	                    activity.finish();
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * 结束指定的Activity
	     */
	    public void finishActivity(Activity activity) {
	        if (activity != null) {
	            activity.finish();
	            activity = null;
	        }
	    }

	    // 杀进程
	    @Override
	    public void onLowMemory() {
	        super.onLowMemory();
	        System.gc();
	    }

	    /**
	     * 把Activity和状态放到List中管理
	     *
	     * @param activity
	     */
	    public void addActivityStatus(Activity activity, boolean isAlive) {
	        if (mapActivity.containsKey(activity.getClass().getName())) {
	            mapActivity.remove(activity.getClass().getName());
	            mapActivity.put(activity.getClass().getName(), isAlive);
	        } else {
	            mapActivity.put(activity.getClass().getName(), isAlive);
	        }

	    }


	    /**
	     * 判断是否有Activity是活动状态
	     *
	     * @return
	     */
	    public boolean isAllActivityAlive() {
	        boolean res = false;
	        Iterator<?> iter = mapActivity.entrySet().iterator();
	        while (iter.hasNext()) {
	            Map.Entry<String, Boolean> entry = (Map.Entry<String, Boolean>) iter
	                    .next();
	            // Object key = entry.getKey();
	            boolean value = entry.getValue();
	            if (value) {
	                return true;
	            }
	        }

	        return res;
	    }


	    public synchronized LruCache<String, Bitmap> getBitmapCache() {
	        if (appBitmapCache == null) {
	            buildCache();
	        }
	        return appBitmapCache;
	    }

	    private void buildCache() {
	        int memClass = ((ActivityManager) getSystemService(
	                Context.ACTIVITY_SERVICE)).getMemoryClass();

	        int cacheSize = Math.max(1024 * 1024 * 8, 1024 * 1024 * memClass / 6);

	        appBitmapCache = new LruCache<String, Bitmap>(cacheSize) {
	            @Override
	            protected int sizeOf(String key, Bitmap bitmap) {

	                return bitmap.getRowBytes() * bitmap.getHeight();
	            }
	        };
	    }

	    //image size
	    private Activity mActivity = null;

	    public Activity getActivity() {
	        return mActivity;
	    }

	    public void setActivity(Activity activity) {
	        this.mActivity = activity;
	    }

	    public Handler getUIHandler() {
	        return handler;
	    }


	    public DisplayMetrics getDisplayMetrics() {
	        if (displayMetrics != null) {
	            return displayMetrics;
	        } else {
	            Activity a = getActivity();
	            if (a != null) {
	                Display display = getActivity().getWindowManager().getDefaultDisplay();
	                DisplayMetrics metrics = new DisplayMetrics();
	                display.getMetrics(metrics);
	                this.displayMetrics = metrics;
	                return metrics;
	            } else {
	                //default screen is 800x480
	                DisplayMetrics metrics = new DisplayMetrics();
	                metrics.widthPixels = 480;
	                metrics.heightPixels = 800;
	                return metrics;
	            }
	        }
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
