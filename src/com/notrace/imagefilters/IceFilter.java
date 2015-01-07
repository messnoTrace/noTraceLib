package com.notrace.imagefilters;

import android.graphics.Bitmap;

/**
 * 冰冻效果
 * @author noTrace
 *
 */
public class IceFilter implements IImageFilter {

	private ImageData mImageData = null;

	public IceFilter(Bitmap bmp) {
		mImageData = new ImageData(bmp);
	}

	@Override
	public ImageData imageProcess() {
		int width = mImageData.getWidth();
		int height = mImageData.getHeight();
		int R, G, B, pixel;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				R = mImageData.getRComponent(x, y);
				G = mImageData.getGComponent(x, y);
				B = mImageData.getBComponent(x, y);

				pixel = R - G - B;
				pixel = pixel * 3 / 2;
				if (pixel < 0)
					pixel = -pixel;
				if (pixel > 255)
					pixel = 255;
				R = pixel; 

				pixel = G - B - R;
				pixel = pixel * 3 / 2;
				if (pixel < 0)
					pixel = -pixel;
				if (pixel > 255)
					pixel = 255;
				G = pixel;

				pixel = B - R - G;
				pixel = pixel * 3 / 2;
				if (pixel < 0)
					pixel = -pixel;
				if (pixel > 255)
					pixel = 255;
				B = pixel;
				mImageData.setPixelColor(x, y, R, G, B);
				

			}
		}

		return mImageData;
	}

}
