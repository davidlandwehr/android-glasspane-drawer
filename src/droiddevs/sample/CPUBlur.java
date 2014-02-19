package droiddevs.sample;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.SystemClock;
import android.util.Log;

public class CPUBlur implements IBlur {
	
	Bitmap temp1;
	Bitmap temp2;
	
	public Bitmap filter(int kernelSize, Bitmap in) {
		
		if (temp1==null || temp1.getWidth()!=in.getWidth() || temp1.getHeight()!=in.getHeight()) {
			temp1 = Bitmap.createBitmap(in.getWidth(), in.getHeight(), Config.ARGB_8888);
			temp2 = Bitmap.createBitmap(in.getWidth(), in.getHeight(), Config.ARGB_8888);
		}
		
    	float[] kernel = KernelUtil.getGaussianKernel(kernelSize);
    	long s= SystemClock.uptimeMillis();
        filterHorizontal(in, temp1, kernel);
        filterVertical(temp1, temp2, kernel);
        long e1= SystemClock.uptimeMillis();
        Log.d("Timing", (e1-s) + "ms filter");
        return temp2;
    }
    
    public void filterHorizontal(Bitmap in, Bitmap out, float[] kernel) {
    	int imageWidth = in.getWidth();
    	int imageWidthMinusOne = imageWidth-1; 
    	int[] row = new int[in.getWidth()];
    	int[] dest = new int[in.getWidth()];
    	int kernelRadius = kernel.length>>1;
    	float kv;
    	int pixel;
    	for (int y=0; y<in.getHeight(); y++) {
    		
    		// Read pixels into array
    		in.getPixels(row, 0, in.getWidth(), 0, y, in.getWidth(), 1);
    		
    		// Process row of pixels
    		for (int x=0; x<imageWidth; x++) {
    			int px = x-kernelRadius;
    			float r=0,g=0,b=0;
    			for (int k=0; k<kernel.length; k++) {
    				kv = kernel[k];
    				
    				// Take care of border condition
    				if (px<0) {
    					pixel = row[0];
    				} else if (px>imageWidthMinusOne){
    					pixel = row[imageWidthMinusOne];
    				} else {
    					pixel = row[px];
    				}
    				
    				// Multiply pixel values
    				r += kv * (pixel&0xff);
    				g += kv * ((pixel>>8)&0xff);
    				b += kv * ((pixel>>16)&0xff);
    				
    				// Process next pixel
    				px++;
    			}
    			//Pack pixel
    			dest[x] = 0xFF000000 | ( ((int)r)&0xff )  | (( ((int)g)&0xff )<<8)  | ( ( ((int)b)&0xff )<<16);
    		}
    		
    		// Write pixels into output array
    		out.setPixels(dest, 0, in.getWidth(), 0, y, in.getWidth(), 1);
    	} 
    }
    
    public void filterVertical(Bitmap in, Bitmap out, float[] kernel) {
    	int imageHeight = in.getHeight();
    	int imageHeightMinusOne = imageHeight-1; 
    	int[] row = new int[in.getHeight()];
    	int[] dest = new int[in.getHeight()];
    	int kernelRadius = kernel.length>>1;
    	float kv;
    	int pixel;
    	for (int x=0; x<in.getWidth(); x++) {
    		
    		// Read pixels into array
    		in.getPixels(row, 0, 1, x, 0, 1, in.getHeight());
    		
    		// Process row of pixels
    		for (int y=0; y<imageHeight; y++) {
    			int py = y-kernelRadius;
    			float r=0,g=0,b=0;
    			for (int k=0; k<kernel.length; k++) {
    				kv = kernel[k];
    				
    				// Take care of border condition
    				if (py<0) {
    					pixel = row[0];
    				} else if (py>imageHeightMinusOne){
    					pixel = row[imageHeightMinusOne];
    				} else {
    					pixel = row[py];
    				}
    				
    				// Multiply pixel values
    				r += kv * (pixel&0xff);
    				g += kv * ((pixel>>8)&0xff);
    				b += kv * ((pixel>>16)&0xff);
    				
    				// Process next pixel
    				py++;
    			}
    			//Pack pixel
    			dest[y] = 0xFF000000 | ( ((int)r)&0xff )  | (( ((int)g)&0xff )<<8)  | ( ( ((int)b)&0xff )<<16);
    		}
    		
    		// Write pixels into output array
    		out.setPixels(dest, 0, 1, x, 0, 1, in.getHeight());
    	} 
    }
}
