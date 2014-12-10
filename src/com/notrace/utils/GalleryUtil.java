package com.notrace.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore.Images.Media;

/**
 * get bitmap from system gallery
 * 
 * @author noTrace
 * 
 */
public class GalleryUtil {

	/**
	 * get bitmap from system gallery
	 * 
	 * no complete
	 * @param context  
	 * @return
	 */
	public static Bitmap getBitmapFromSystem(Context context) {
		
		//TODO to complete
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		String[] columns = new String[] { Media.DATA, Media._ID, Media.TITLE,
				Media.DISPLAY_NAME };
		Cursor cursor = context.getContentResolver().query(
				Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
		int photoIndex = cursor.getColumnIndexOrThrow(Media.DATA);
		int nameIndex = cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME);
		if (cursor.moveToFirst()) {
			String photoPath = cursor.getString(photoIndex);
			Bitmap bm = BitmapUtil.decodeBitmapFromPath(photoPath);
			return bm;
		}

		return null; 
	}
}
