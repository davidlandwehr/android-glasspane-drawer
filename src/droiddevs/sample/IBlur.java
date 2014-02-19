package droiddevs.sample;

import android.graphics.Bitmap;

public interface IBlur {
	
	Bitmap filter(int kernelSize, Bitmap in);
}
