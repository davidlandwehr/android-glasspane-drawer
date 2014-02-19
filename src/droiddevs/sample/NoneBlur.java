package droiddevs.sample;

import android.graphics.Bitmap;

public class NoneBlur implements IBlur 
{

	@Override
	public Bitmap filter(int kernelSize, Bitmap in) {
		return in;
	}

}
