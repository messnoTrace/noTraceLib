package com.notrace.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * bitmap util
 * 
 * @author noTrace
 * 
 */
public class BitmapUtil {

	private static final float DISPLAY_WIDTH = 200;
	private static final float DISPLAY_HEIGHT = 200;

	public static Bitmap decodeBitmapFromPath(String path) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeFile(path, op);
		// 获取比例大小
		int wRatio = (int) Math.ceil(op.outWidth / DISPLAY_WIDTH);
		int hRatio = (int) Math.ceil(op.outHeight / DISPLAY_HEIGHT);

		// 如果超出指定大小，则缩小比例
		if (wRatio > 1 && hRatio > 1) {
			if (wRatio > hRatio) {
				op.inSampleSize = wRatio;
			} else {
				op.inSampleSize = hRatio;
			}
		}

		op.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, op);
		return bm;
	}
}
