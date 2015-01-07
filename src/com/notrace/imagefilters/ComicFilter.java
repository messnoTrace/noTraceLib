package com.notrace.imagefilters;

import com.notrace.utils.ImageUtil;

import android.graphics.Bitmap;

public class ComicFilter implements IImageFilter{

	
	private ImageData mImageData=null;
	
	public  ComicFilter(Bitmap bmp) {
		mImageData=new ImageData(bmp);
	}
	@Override
	public ImageData imageProcess() {
		
		int width=mImageData.getWidth();
		int height=mImageData.getHeight();	
		
		int R, G, B, pixel;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				R = mImageData.getRComponent(x, y); // 获取RGB三原色
				G = mImageData.getGComponent(x, y);
				B = mImageData.getBComponent(x, y);

				// R = |g – b + g + r| * r / 256;
				pixel = G - B + G + R;
				if (pixel < 0)
					pixel = -pixel;
				pixel = pixel * R / 256;
				if (pixel > 255)
					pixel = 255;
				R = pixel;

				// G = |b – g + b + r| * r / 256;
				pixel = B - G + B + R;
				if (pixel < 0)
					pixel = -pixel;
				pixel = pixel * R / 256;
				if (pixel > 255)
					pixel = 255;
				G = pixel;

				// B = |b – g + b + r| * g / 256;
				pixel = B - G + B + R;
				if (pixel < 0)
					pixel = -pixel;
				pixel = pixel * G / 256;
				if (pixel > 255)
					pixel = 255;
				B = pixel;
				mImageData.setPixelColor(x, y, R, G, B);
			}
		}
		
		Bitmap bitmap=mImageData.getDstBitmap();
		
		bitmap=ImageUtil.toGrayscale(bitmap);
		mImageData=new ImageData(bitmap);
		
		return mImageData;
	}

}
