package com.notrace.imagefilters;

import android.graphics.Bitmap;


public class LomoFilter implements IImageFilter {

	private BrightContrastFilter contrastFx;
	private GradientMapFilter gradientMapFx;
	private ImageBlender blender = new ImageBlender();
	private VignetteFilter vignetteFX;
	private NoiseFilter noiseFx;

	private ImageData image;
	
	public LomoFilter(Bitmap bm) {
		image = new ImageData(bm);
		contrastFx = new BrightContrastFilter(image);
		contrastFx.brightnessFactor = 0.05f;
        contrastFx.contrastFactor = 0.5f;
     
        blender.Mixture = 0.5f;
        blender.Mode = ImageBlender.BlendMode.Multiply;
    
        vignetteFX = new VignetteFilter(image);
        vignetteFX.Size = 0.6f;

	}
	public ImageData imageProcess() {
		// TODO Auto-generated method stub
		 ImageData tempImg = contrastFx.imageProcess();
		 noiseFx = new NoiseFilter(tempImg);
		 noiseFx.Intensity = 0.02f;
         tempImg = noiseFx.imageProcess();
         gradientMapFx = new GradientMapFilter(tempImg);
         image = gradientMapFx.imageProcess();
         image = blender.Blend(image, tempImg);
         vignetteFX = new VignetteFilter(image);
         vignetteFX.Size = 0.6f;
         image = vignetteFX.imageProcess();
         return image;
	}

}
