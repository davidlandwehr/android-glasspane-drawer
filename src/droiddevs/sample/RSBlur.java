package droiddevs.sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.SystemClock;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

public class RSBlur implements IBlur {
	
	Bitmap temp1;
	Bitmap temp2;

	private RenderScript mRS;
    private Allocation mInAllocation;
    private Allocation mOutAllocation;
    private Allocation mOutAllocation2;
    private Allocation mKernel;
    private ScriptC_GaussianBlur mScript;

    public RSBlur(Activity activity) {
    	mRS = RenderScript.create(activity);

        mScript = new ScriptC_GaussianBlur(mRS);
        
    } 
    
    
	public Bitmap filter(int kernelSize, Bitmap in) {
		
		if (temp1==null || temp1.getWidth()!=in.getWidth() || temp1.getHeight()!=in.getHeight()) {
			temp1 = Bitmap.createBitmap(in.getWidth(), in.getHeight(), Config.ARGB_8888);
			temp2 = Bitmap.createBitmap(in.getWidth(), in.getHeight(), Config.ARGB_8888);
			
			mInAllocation = Allocation.createFromBitmap(mRS, in,
				                  Allocation.MipmapControl.MIPMAP_NONE,
				                  Allocation.USAGE_SHARED | 
				                  Allocation.USAGE_GRAPHICS_TEXTURE |
				                  Allocation.USAGE_SCRIPT);
			mOutAllocation = Allocation.createFromBitmap(mRS, temp1,
			                      Allocation.MipmapControl.MIPMAP_NONE,
			                      Allocation.USAGE_SCRIPT |
			                      Allocation.USAGE_GRAPHICS_TEXTURE |
			                      Allocation.USAGE_SHARED);
			
			mOutAllocation2 = Allocation.createFromBitmap(mRS, temp2,
									Allocation.MipmapControl.MIPMAP_NONE,
									Allocation.USAGE_SCRIPT |
									Allocation.USAGE_GRAPHICS_TEXTURE |
									Allocation.USAGE_SHARED);
		} else {
			mInAllocation.copyFrom(in);
		}
		
		float[] kernel = KernelUtil.getGaussianKernel(kernelSize);
    	mKernel = Allocation.createSized(mRS, Element.F32(mRS), kernel.length);
        mKernel.copyFrom(kernel);
        
        mScript.set_pixels(mInAllocation);
        mScript.set_kernelsize(kernel.length);
        mScript.set_width(in.getWidth());
        mScript.set_height(in.getHeight());
        mScript.bind_gaussian(mKernel);
        long s= SystemClock.uptimeMillis();
        mScript.forEach_blurhorizontal(mInAllocation, mOutAllocation);
        mScript.set_pixels(mOutAllocation);
        mScript.forEach_blurvertical(mOutAllocation, mOutAllocation2);
        long e1= SystemClock.uptimeMillis();
        mOutAllocation2.copyTo(temp2);
        long e2= SystemClock.uptimeMillis();
        Log.d("Timing", (e1-s) + "ns for script");
        Log.d("Timing", (e2-e1) + "ns for readout to bitmap");
	    return temp2;
    }
}
