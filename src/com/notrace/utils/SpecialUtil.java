package com.notrace.utils;

import com.notrace.imagefilters.ComicFilter;
import com.notrace.imagefilters.FeatherFilter;
import com.notrace.imagefilters.GlowingEdgeFilter;
import com.notrace.imagefilters.IceFilter;
import com.notrace.imagefilters.LomoFilter;
import com.notrace.imagefilters.MoltenFilter;
import com.notrace.imagefilters.SoftGlowFilter;
import com.notrace.imagefilters.ZoomBlurFilter;

import android.graphics.Bitmap;

/**
 * 这些操作耗时，应该另开线程
 * 
 * @author noTrace
 *
 */
public class SpecialUtil {
	/**
	 * 冰冻效果
	 */
	public static Bitmap ice(Bitmap bmp) {
		return new IceFilter(bmp).imageProcess().getDstBitmap();
	}

	/**
	 * 铸溶
	 * 
	 * @return
	 */
	public static Bitmap molten(Bitmap bmp) {
		return new MoltenFilter(bmp).imageProcess().getDstBitmap();
	}

	/**
	 * 连环画
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap comic(Bitmap bmp) {
		return new ComicFilter(bmp).imageProcess().getDstBitmap();
	}

	/**
	 * 柔化美白
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap softGlow(Bitmap bmp) {
		return new SoftGlowFilter(bmp).imageProcess().getDstBitmap();
	}

	/**
	 * 照亮边缘
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap glowingEdge(Bitmap bmp) {
		return new GlowingEdgeFilter(bmp).imageProcess().getDstBitmap();
	}

	/**
	 * 羽化效果
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap feather(Bitmap bitmap) {
		return new FeatherFilter(bitmap).imageProcess().getDstBitmap();
	}

	/**
	 * 缩放模糊
	 * 
	 * @param bitmap
	 * @param length
	 * @return
	 */
	public static Bitmap zoomBlur(Bitmap bitmap, int length) {
		// test length=30
		return new ZoomBlurFilter(bitmap, length).imageProcess().getDstBitmap();

	}

	/**
	 * lomo
	 * @param bitmap
	 * @return
	 */
	public static Bitmap lomo(Bitmap bitmap) {
		return new LomoFilter(bitmap).imageProcess().getDstBitmap();
	}

}
