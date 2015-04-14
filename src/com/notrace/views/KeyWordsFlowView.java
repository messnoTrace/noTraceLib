package com.notrace.views;

import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Interpolator;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
* @ClassName KeyWordsFlowView
* @Description 
* @Author notrace
* @Date date{time}
*/ 
public class KeyWordsFlowView extends FrameLayout implements OnGlobalLayoutListener{

	public static final int INDEX_X=0;
	public static final int INDEX_Y=1;
	public static final int INDEX_TEXT_LENGTH=2;
	public static final int INDEX_DIS_Y=3;
	
	public static final int ANIMATION_IN=1;
	public static final int ANIMATION_OUT=2;
	//动画类型:	 
	//从外围到坐标点
	public static final int OUTSIDE_TO_LOCATION=1;
	//从坐标点到外围
	public static final int LOCATION_TO_OUTSIDE=2;
	//从中心点到坐标点
	public static final int CENTER_TO_LOCATION=3;
	//从坐标点到中心点
	public static final int LOCATION_TO_CENTER=4;
	public static final long ANIMATION_DURATION=8000;
	
	public static int MAX=20;
	
	public static final int TEXT_SIZE_MAX=16;
	public static final int TEXT_SIZE_MIN=16;
	
	private OnClickListener itemClickListener;
	private static Interpolator interpolator;
	
	private static AlphaAnimation animAlpha2Opaque;
	private static AlphaAnimation animAlpha2Transparent;
	private static ScaleAnimation animScaleLarger2Normal,animScaleNormal2Large,animScaleZero2Normal,animScaleNormal2Zero;

	//存储关键字
	private Vector<String> vecKeywords;
	
	private int width,height;
	
	/**
	 * go2Show()中被赋值为true 标识开发人员触发其开始动画显示
	 * 本标示的作用是防止在填充keywords 未完成的过程中获取到width和height后提前启动动画
	 * 在show（）方法中被赋值为false
	 * 真正能够显示动画的另一必要条件 width和height不为0
	 */
	private boolean enableShow;
	private Random random;
	
	
	/**
	 * @see ANIMATION_IN
	 * @see ANIMATION_OUT
	 * @see OUTSIDE_TO_LOCATION
	 * @see LOCATION_TO_OUTSIDE
	 * @see LOCATION_TO_CENTER
	 * @see CENTER_TO_LOCATION
	 * */
	
	private int txtAnimInType,txtAnimOutType;
	
	private long lastStartAnimationTime;
	
	private long animDuration;
	
	
	
	public KeyWordsFlowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public KeyWordsFlowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public KeyWordsFlowView(Context context) {
		super(context);
		init();
	}

	
	
	private void init()
	{
		
		lastStartAnimationTime=0l;
		animDuration=ANIMATION_DURATION;
		random=new Random();
		vecKeywords=new Vector<String>(MAX);
		
		getViewTreeObserver().addOnGlobalLayoutListener(this);
		interpolator =(Interpolator) android.view.animation.AnimationUtils.loadInterpolator(getContext(),android.R.anim.decelerate_interpolator);
		
		animAlpha2Opaque =new AlphaAnimation(0.0f,1.0f);
		animAlpha2Transparent = new AlphaAnimation(1.0f, 0.0f);
		animScaleLarger2Normal = new ScaleAnimation(2, 1, 2, 1);
		animScaleNormal2Large = new ScaleAnimation(1, 2, 1, 2);
		animScaleZero2Normal = new ScaleAnimation(0, 1, 0, 1);
		animScaleNormal2Zero = new ScaleAnimation(1, 0, 1, 0);
	}
	
	public long getDuration() {
		return animDuration;
	}

	public void setDuration(long duration) {
		animDuration = duration;
	}

	public boolean feedKeyword(String keyword) {
		boolean result = false;
		if (vecKeywords.size() < MAX) {
			result = vecKeywords.add(keyword);
		}
		return result;
	}
	
	/**
	 * 
	* @Title go2Show 开始动画显示
	* @Description 
	* @param animType
	* @return 
	* @Return boolean 正常显示动画返回true 反之返回false
	* 			返回false 原因如下
	* 
	* 			1.时间上不允许。受lastStartAnimationTime的制约
	* 			2.未获取到widht和height的值
	* @Throws
	 */
	
	
	public boolean go2Show(int animType) {
		if (System.currentTimeMillis() - lastStartAnimationTime > animDuration) {
			enableShow = true;
			if (animType == ANIMATION_IN) {
				txtAnimInType = OUTSIDE_TO_LOCATION;
				txtAnimOutType = LOCATION_TO_CENTER;
			} else if (animType == ANIMATION_OUT) {
				txtAnimInType = CENTER_TO_LOCATION;
				txtAnimOutType = LOCATION_TO_OUTSIDE;
			}
			disapper();
			boolean result = show();
			return result;
		}
		return false;
	}
	private void disapper(){
		int size=getChildCount();
		for (int i=size-1;i>=0;i--) {
			final TextView txt=(TextView) getChildAt(i);
			if(txt.getVisibility()==View.GONE){
				removeView(txt);
				continue;
			}
			
			FrameLayout.LayoutParams layParams=(LayoutParams) txt.getLayoutParams();
			int[]xy=new int[]{layParams.leftMargin,layParams.topMargin,txt.getWidth()};
			AnimationSet animSet=getAnimationSet(xy,(width>>1),(height>>1),txtAnimOutType);
			txt.startAnimation(animSet);
			animSet.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					txt.setOnClickListener(null);
					txt.setClickable(false);
					txt.setVisibility(View.GONE);
				}
			});
		}
	}
	
	private int[] colorStr={0xff669900, 0xff0099ff, 0xffff6600,
			0xffff3333, 0xff666666};
	
	
	private boolean show(){
		if(width>0 && height>0 && vecKeywords !=null&& vecKeywords.size() > 0 && enableShow){
			enableShow = false;
			lastStartAnimationTime = System.currentTimeMillis();
			// 找到中心点
			int xCenter = width >> 1, yCenter = height >> 1;
			// 关键字的个数。
			int size = vecKeywords.size();
			int xItem = width / size, yItem = height / size;
			// Log.d("ANDROID_LAB", "--------------------------width=" + width +
			// " height=" + height + "  xItem=" + xItem
			// + " yItem=" + yItem + "---------------------------");
			LinkedList<Integer> listX = new LinkedList<Integer>(), listY = new LinkedList<Integer>();
			for (int i = 0; i < size; i++) {
				// 准备随机候选数，分别对应x/y轴位置
				listX.add(i * xItem);
				listY.add(i * yItem + (yItem >> 2));
				// Log.e("Search", "ListX:"+(i * xItem)+"#listY:"+(i * yItem +
				// (yItem >> 2)));
			}
			// TextView[] txtArr = new TextView[size];
			LinkedList<TextView> listTxtTop = new LinkedList<TextView>();
			LinkedList<TextView> listTxtBottom = new LinkedList<TextView>();
			for (int i = 0; i < size; i++) {
				String keyword = vecKeywords.get(i);
				// 随机颜色
				// int ranColor = 0xff000000 | random.nextInt(0x0077ffff);
				//随机颜色
				// int index=(int) (Math.random()*colorStr.length);
				// 固定颜色
				int ranColor = 0xff666666;
				// 随机位置，糙值
				int xy[] = randomXY(random, listX, listY, xItem);
				// 随机字体大小
				int txtSize = TEXT_SIZE_MIN
						+ random.nextInt(TEXT_SIZE_MAX - TEXT_SIZE_MIN + 1);
				// 实例化TextView
				final TextView txt = new TextView(getContext());
				txt.setOnClickListener(itemClickListener);
				txt.setText(keyword);
				txt.setTextColor(ranColor);
				txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
				txt.setPadding(5, 5, 5, 5);
				// 设置阴影
				// txt.setShadowLayer(1, 1, 1, 0xdd696969);
				txt.setGravity(Gravity.CENTER);

				// txt.setBackgroundColor(ranColor);
				// 获取文本长度
				Paint paint = txt.getPaint();
				int strWidth = 0;
				try {
					strWidth = (int) Math.ceil(paint.measureText(keyword));
				} catch (Exception e) {
					e.printStackTrace();
				}
				xy[INDEX_TEXT_LENGTH] = strWidth;
				// 第一次修正:修正x坐标
				if (xy[INDEX_X] + strWidth > width - (xItem >> 1)) {
					int baseX = width - strWidth;
					// 减少文本右边缘一样的概率
					xy[INDEX_X] = baseX - xItem + random.nextInt(xItem >> 1);
				} else if (xy[INDEX_X] == 0) {
					// 减少文本左边缘一样的概率
					xy[INDEX_X] = Math.max(random.nextInt(xItem), xItem / 3);
				}
				xy[INDEX_DIS_Y] = Math.abs(xy[INDEX_Y] - yCenter);
				txt.setTag(xy);
				if (xy[INDEX_Y] > yCenter) {
					listTxtBottom.add(txt);
				} else {
					listTxtTop.add(txt);
				}
			}
			attach2Screen(listTxtTop, xCenter, yCenter, yItem);
			attach2Screen(listTxtBottom, xCenter, yCenter, yItem);
			return true;
		}
		return false;
	}
	
	
	/** 修正TextView的Y坐标将将其添加到容器上。 */
	private void attach2Screen(LinkedList<TextView> listTxt, int xCenter,
			int yCenter, int yItem) {
		int size = listTxt.size();
		sortXYList(listTxt, size);
		for (int i = 0; i < size; i++) {
			TextView txt = listTxt.get(i);
			int[] iXY = (int[]) txt.getTag();
			// 第二次修正:修正y坐标
			int yDistance = iXY[INDEX_Y] - yCenter;
			// 对于最靠近中心点的，其值不会大于yItem<br/>
			// 对于可以一路下降到中心点的，则该值也是其应调整的大小<br/>
			int yMove = Math.abs(yDistance);
			inner: for (int k = i - 1; k >= 0; k--) {
				int[] kXY = (int[]) listTxt.get(k).getTag();
				int startX = kXY[INDEX_X];
				int endX = startX + kXY[INDEX_TEXT_LENGTH];
				// y轴以中心点为分隔线，在同一侧
				if (yDistance * (kXY[INDEX_Y] - yCenter) > 0) {
					// Log.d("ANDROID_LAB", "compare:" +
					// listTxt.get(k).getText());
					if (isXMixed(startX, endX, iXY[INDEX_X], iXY[INDEX_X]
							+ iXY[INDEX_TEXT_LENGTH])) {
						int tmpMove = Math.abs(iXY[INDEX_Y] - kXY[INDEX_Y]);
						if (tmpMove > yItem) {
							yMove = tmpMove;
						} else if (yMove > 0) {
							// 取消默认值。
							yMove = 0;
						}
						// Log.d("ANDROID_LAB", "break");
						break inner;
					}
				}
			}
			if (yMove > yItem) {
				int maxMove = yMove - yItem;
				int randomMove = random.nextInt(maxMove);
				int realMove = Math.max(randomMove, maxMove >> 1) * yDistance
						/ Math.abs(yDistance);
				iXY[INDEX_Y] = iXY[INDEX_Y] - realMove;
				iXY[INDEX_DIS_Y] = Math.abs(iXY[INDEX_Y] - yCenter);
				// 已经调整过前i个需要再次排序
				sortXYList(listTxt, i + 1);
			}
			FrameLayout.LayoutParams layParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			layParams.gravity = Gravity.LEFT | Gravity.TOP;
			layParams.leftMargin = iXY[INDEX_X];
			layParams.topMargin = iXY[INDEX_Y];
			addView(txt, layParams);
			// 动画
			AnimationSet animSet = getAnimationSet(iXY, xCenter, yCenter,
					txtAnimOutType);
			txt.startAnimation(animSet);
		}
	}

	public AnimationSet getAnimationSet(int[] xy, int xCenter, int yCenter,
			int type) {
		AnimationSet animSet = new AnimationSet(true);
		animSet.setInterpolator((android.view.animation.Interpolator) interpolator);
		if (type == OUTSIDE_TO_LOCATION) {
			animSet.addAnimation(animAlpha2Opaque);
			animSet.addAnimation(animScaleLarger2Normal);
			TranslateAnimation translate = new TranslateAnimation((xy[INDEX_X]
					+ (xy[INDEX_TEXT_LENGTH] >> 1) - xCenter) << 1, 0,
					(xy[INDEX_Y] - yCenter) << 1, 0);
			animSet.addAnimation(translate);
		} else if (type == LOCATION_TO_OUTSIDE) {
			animSet.addAnimation(animAlpha2Transparent);
			animSet.addAnimation(animScaleNormal2Large);
			TranslateAnimation translate = new TranslateAnimation(0, (xy[INDEX_X]
					+ (xy[INDEX_TEXT_LENGTH] >> 1) - xCenter) << 1, 0,
					(xy[INDEX_Y] - yCenter) << 1);
			animSet.addAnimation(translate);
		} else if (type == LOCATION_TO_CENTER) {
			animSet.addAnimation(animAlpha2Transparent);
			animSet.addAnimation(animScaleNormal2Zero);
			TranslateAnimation translate = new TranslateAnimation(0,
					(-xy[INDEX_X] + xCenter), 0, (-xy[INDEX_Y] + yCenter));
			animSet.addAnimation(translate);
		} else if (type == CENTER_TO_LOCATION) {
			animSet.addAnimation(animAlpha2Opaque);
			animSet.addAnimation(animScaleZero2Normal);
			TranslateAnimation translate = new TranslateAnimation(
					(-xy[INDEX_X] + xCenter), 0, (-xy[INDEX_Y] + yCenter), 0);
			animSet.addAnimation(translate);
		}
		animSet.setDuration(animDuration);
		return animSet;
	}

	/**
	 * 根据与中心点的距离由近到远进行冒泡排序。
	 * 
	 * @param endIdx
	 *            起始位置。
	 * @param txtArr
	 *            待排序的数组。
	 * 
	 */
	private void sortXYList(LinkedList<TextView> listTxt, int endIdx) {
		for (int i = 0; i < endIdx; i++) {
			for (int k = i + 1; k < endIdx; k++) {
				if (((int[]) listTxt.get(k).getTag())[INDEX_DIS_Y] < ((int[]) listTxt
						.get(i).getTag())[INDEX_DIS_Y]) {
					TextView iTmp = listTxt.get(i);
					TextView kTmp = listTxt.get(k);
					listTxt.set(i, kTmp);
					listTxt.set(k, iTmp);
				}
			}
		}
	}

	/** A线段与B线段所代表的直线在X轴映射上是否有交集。 */
	private boolean isXMixed(int startA, int endA, int startB, int endB) {
		boolean result = false;
		if (startB >= startA && startB <= endA) {
			result = true;
		} else if (endB >= startA && endB <= endA) {
			result = true;
		} else if (startA >= startB && startA <= endB) {
			result = true;
		} else if (endA >= startB && endA <= endB) {
			result = true;
		}
		return result;
	}

	private int[] randomXY(Random ran, LinkedList<Integer> listX,
			LinkedList<Integer> listY, int xItem) {
		int[] arr = new int[4];
		arr[INDEX_X] = listX.remove(ran.nextInt(listX.size()));
		arr[INDEX_Y] = listY.remove(ran.nextInt(listY.size()));
		return arr;
	}
	
	@Override
	public void onGlobalLayout() {
		int tmpW = getWidth();
		int tmpH = getHeight();
		if (width != tmpW || height != tmpH) {
			width = tmpW;
			height = tmpH;
			show();
		}
	}
	
	public Vector<String> getKeywords() {
		return vecKeywords;
	}

	public void rubKeywords() {
		vecKeywords.clear();
	}

	/** 直接清除所有的TextView。在清除之前不会显示动画。 */
	public void rubAllViews() {
		removeAllViews();
	}

	public void setOnItemClickListener(OnClickListener listener) {
		itemClickListener = listener;
	}


}
