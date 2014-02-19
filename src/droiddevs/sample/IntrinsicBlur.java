package droiddevs.sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.SystemClock;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

public class IntrinsicBlur implements IBlur {
	
	Bitmap temp1;

	private RenderScript mRS;
    private Allocation mInAllocation;
    private Allocation mOutAllocation;

    
    ScriptIntrinsicBlur intrinsic;
    public IntrinsicBlur(Activity activity) {
    	mRS = RenderScript.create(activity);
    	intrinsic = ScriptIntrinsicBlur.create(mRS, Element.U8_4(mRS));
        
        
    } 
    
    
	public Bitmap filter(int kernelSize, Bitmap in) {
		if (temp1==null || temp1.getWidth()!=in.getWidth() || temp1.getHeight()!=in.getHeight()) {
			temp1 = Bitmap.createBitmap(in.getWidth(), in.getHeight(), Config.ARGB_8888);
			
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
			
		} else {
			mInAllocation.copyFrom(in);
		}
		
		float kernelS = kernelSize;
		kernelS = Math.max(0.1f, Math.min(25, kernelS));
		intrinsic.setRadius(kernelS);
		intrinsic.setInput(mInAllocation);
		intrinsic.forEach(mOutAllocation);
	    mOutAllocation.copyTo(temp1);
        
	    return temp1;
    }
}
