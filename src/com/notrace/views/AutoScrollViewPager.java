package com.notrace.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 循环滚动切换图片(支持带标题,不带标题传null即可),自带适配器 支持显示本地res图片和网络图片，指定uri的图片
 * OnPagerClickCallback onPagerClickCallback 处理page被点击的回调接口 被触摸时，暂停滚动，增强用户友好性
 * 
 * @author dance
 */
@SuppressLint({ "ViewConstructor", "HandlerLeak" })
public class AutoScrollViewPager extends ViewPager {
	@SuppressWarnings("unused")
	private String TAG = "RollViewPager";
	private Context context;
	private int currentItem = 0;
	private List<String> uriList;// 图片地址
	private ArrayList<View> dots;// 点的位置不固定，所以需要让调用者传入
	private TextView title;// 用于显示每个图片的标题
	private String[] titles;
	private int[] resImageIds;
	private int dot_focus_resId;// 获取焦点的图片id
	private int dot_normal_resId;// 未获取焦点的图片id
	private OnPagerClickCallback onPagerClickCallback;
	private boolean isShowResImage = false;
	MyOnTouchListener myOnTouchListener;
	ViewPagerTask viewPagerTask;
	private int size = 0;
	private long start = 0;
	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();

	public class MyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				start = System.currentTimeMillis();
				handler.removeCallbacksAndMessages(null);
				// ((ImageView)
				// v).setColorFilter(Color.parseColor("#50000000"));
				break;
			case MotionEvent.ACTION_MOVE:
				handler.removeCallbacks(viewPagerTask);
				break;
			case MotionEvent.ACTION_CANCEL:
				// ((ImageView)
				// v).setColorFilter(Color.parseColor("#00000000"));
				startRoll();
				break;
			case MotionEvent.ACTION_UP:
				// ((ImageView)
				// v).setColorFilter(Color.parseColor("#00000000"));
				long duration = System.currentTimeMillis() - start;
				if (duration <= 400) {
					if (onPagerClickCallback != null)
						onPagerClickCallback.onPagerClick(currentItem % size);
				}
				startRoll();
				break;
			}

			// 修改触摸事件冲突，暂时采用这种方式
			curP.x = event.getX();
			curP.y = event.getY();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				downP.x = event.getX();
				downP.y = event.getY();
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				float dx = Math.abs(event.getX() - downP.x);
				float dy = Math.abs(event.getY() - downP.y);
				if (dy < dx) {
					getParent().requestDisallowInterceptTouchEvent(true);
				} else {
					getParent().requestDisallowInterceptTouchEvent(false);
				}
			}
			return true;
		}
	}

	public class ViewPagerTask implements Runnable {
		@Override
		public void run() {
			if (currentItem == Integer.MAX_VALUE) {
				currentItem = 0;
			}
			currentItem++;
			handler.obtainMessage().sendToTarget();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			AutoScrollViewPager.this.setCurrentItem(currentItem);
			startRoll();
		}
	};
	private ImageLoader mBU;
	private DisplayImageOptions options;

	/**
	 * 循环滚动切换图片，被触摸时，暂停滚动，增强用户友好性 ，支持带标题,设置标题用setTitle，
	 * 支持显示本地res图片和网络图片，指定uri的图片
	 * 
	 * @param context
	 * @param dots
	 *            图片的点的集合，之所以不自动生成，是因为点的位置和大小不确定，所以由调用者传入
	 * @param onPagerClickCallback
	 *            页面点击回调
	 * @param dot_focus_resId
	 *            获取焦点的图片id
	 * @param dot_normal_resId
	 *            未获取焦点的图片id
	 */
	public AutoScrollViewPager(Context context, ArrayList<View> dots,
			int dot_focus_resId, int dot_normal_resId,
			OnPagerClickCallback onPagerClickCallback) {
		super(context);
		this.context = context;
		this.dots = dots;
		this.dot_focus_resId = dot_focus_resId;
		this.dot_normal_resId = dot_normal_resId;
		this.onPagerClickCallback = onPagerClickCallback;
		if (dots != null && dots.size() > 0)
			dots.get(0).setBackgroundResource(dot_focus_resId);
		mBU = ImageLoader.getInstance();
//		options = new DisplayImageOptions.Builder().cacheInMemory(true)
//				.cacheOnDisc(true).resetViewBeforeLoading(true)
//				.showStubImage(R.drawable.home_theme_bg_loading)
//				.showImageOnFail(R.drawable.home_theme_bg_loading)
//				.showImageForEmptyUri(R.drawable.home_theme_bg_loading).build();
//		 options.setShowOriginal(true);
		viewPagerTask = new ViewPagerTask();
		myOnTouchListener = new MyOnTouchListener();
	}

	/**
	 * 设置网络图片的url集合，也可以是本地图片的uri
	 * 图片uriList集合，可以是网络地址，如：http://www.ssss.cn/3.jpg，也可以是本地的uri,如：
	 * assest://image/3.jpg，uriList和resImageIds只需传入一个
	 * 
	 * @param uriList
	 */
	public void setUriList(List<String> uriList) {
		isShowResImage = false;
		if (null != uriList) {
			size = uriList.size();
		}
		this.uriList = uriList;
	}

	/**
	 * 设置res图片的id 图片uriList集合，可以是网络地址，如：http://www.ssss.cn/3.jpg，也可以是本地的uri,如：
	 * assest://image/3.jpg，uriList和resImageIds只需传入一个
	 * 
	 * @param resImageIds
	 */
	public void setResImageIds(int[] resImageIds) {
		isShowResImage = true;
		if (null != resImageIds) {
			size = resImageIds.length;
		}
		this.resImageIds = resImageIds;
	}

	/**
	 * 标题相关
	 * 
	 * @param title
	 *            用于显示标题的TextView
	 * @param titles
	 *            标题数组
	 */
	public void setTitle(TextView title, String[] titles) {
		this.title = title;
		this.titles = titles;
		if (title != null && titles != null && titles.length > 0)
			title.setText(titles[0]);// 默认显示第一张的标题
	}

	private boolean hasSetAdapter = false;

	/**
	 * 开始滚动
	 */
	public void startRoll() {
		if (!hasSetAdapter) {
			hasSetAdapter = true;
			this.setOnPageChangeListener(new MyOnPageChangeListener());
			this.setAdapter(new ViewPagerAdapter());
			this.currentItem = size * 1000;
			this.setCurrentItem(currentItem);
			if (dots != null && dots.size() > 0)
				dots.get(0).setBackgroundResource(dot_focus_resId);
		}

		handler.removeCallbacks(viewPagerTask);
		handler.postDelayed(viewPagerTask, 3000);
	}

	public void stopRoll() {
		handler.removeCallbacks(viewPagerTask);
	}

	class MyOnPageChangeListener implements OnPageChangeListener {
		int oldPosition = 0;

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			if (title != null)
				title.setText(titles[position % size]);
			if (dots != null && dots.size() > 0) {
				dots.get(position % size)
						.setBackgroundResource(dot_focus_resId);
				dots.get(oldPosition % size).setBackgroundResource(
						dot_normal_resId);
				if (dots.size() == 1)
					dots.get(0).setBackgroundResource(dot_focus_resId);
			}
			oldPosition = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	/**
	 * 自带设配器,需要回调类来处理page的点击事件
	 * 
	 * @author dance
	 */
	class ViewPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			if (uriList != null && uriList.size() == 1)
				return 1;
			else
				return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			// View view = View.inflate(context, R.layout.viewpager_item, null);

			ImageView view = new ImageView(context);
			((ViewPager) container).addView(view);
			view.setScaleType(ScaleType.FIT_XY);
			view.setOnTouchListener(myOnTouchListener);
			// ImageView imageView = (ImageView) view.findViewById(R.id.image);
			if (isShowResImage) {
				// imageView.setImageResource(resImageIds[position%size]);
				view.setImageResource(resImageIds[position % size]);
			} else {
				// mBU.displayImage(uriList.get(position%size), imageView,
				// options);
				
//				mBU.displayImage(uriList.get(position % size), view, options);
				mBU.displayImage(uriList.get(position % size), view);
			}
			return view;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// 将ImageView从ViewPager移除
			((ViewPager) arg0).removeView((View) arg2);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		handler.removeCallbacksAndMessages(null);
		super.onDetachedFromWindow();
	}

	/**
	 * 处理page点击的回调接口
	 * 
	 * @author dance
	 */
	public interface OnPagerClickCallback {
		public abstract void onPagerClick(int position);
	}
}
