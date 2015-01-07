package com.notrace.imagefilters;

import android.graphics.Bitmap;


/**
 * 高亮对比度特效
 * @author noTrace
 *
 */
public class BrightContrastFilter implements IImageFilter{

	private ImageData mImageData=null;
	
	public float brightnessFactor=0.25f;
	
	public float contrastFactor=0f;
	
	 public BrightContrastFilter(Bitmap bmp) {
		 mImageData=new ImageData(bmp);
	}
	 public BrightContrastFilter(ImageData imageData) {
		 this.mImageData=imageData;
		
	}

	@Override
	public ImageData imageProcess() {
		
		int width = mImageData.getWidth();
		int height = mImageData.getHeight();
		int r, g, b;
		// Convert to integer factors
		int bfi = (int) (brightnessFactor * 255);
		float cf = 1f + contrastFactor;
		cf *= cf;
		int cfi = (int) (cf * 32768) + 1;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				r = mImageData.getRComponent(x, y);
				g = mImageData.getGComponent(x, y);
				b = mImageData.getBComponent(x, y);
				// Modify brightness (addition)
				if (bfi != 0) {
					// Add brightness
					int ri = r + bfi;
					int gi = g + bfi;
					int bi = b + bfi;
					// Clamp to byte boundaries
					r = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
					g = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
					b = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
				}
				// Modifiy contrast (multiplication)
				if (cfi != 32769) {
					// Transform to range [-128, 127]
					int ri = r - 128;
					int gi = g - 128;
					int bi = b - 128;

					// Multiply contrast factor
					ri = (ri * cfi) >> 15;
					gi = (gi * cfi) >> 15;
					bi = (bi * cfi) >> 15;

					// Transform back to range [0, 255]
					ri = ri + 128;
					gi = gi + 128;
					bi = bi + 128;

					// Clamp to byte boundaries
					r = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
					g = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
					b = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
				}
				mImageData.setPixelColor(x, y, r, g, b);
			}
		}
		
		return mImageData;
	}
	

}
