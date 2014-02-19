package droiddevs.sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

public class ScreenDumpUtils {

	Activity activity;
	View rootView;
	Bitmap bitmap;
	int factor;
	float kernel = 150;
	public long screenshot;
	public long filtering;
	
	public ScreenDumpUtils(Activity context, View rootView, int factor) {
		this.activity = context;
		this.rootView = rootView;
		this.factor = factor;
	}
	
	public void setFactor(int factor) {
		if (this.factor!=factor) {
			this.factor = factor;
			bitmap = null;
		}
	}
	
	public void setKernel(int kernel) {
		this.kernel = kernel;
	}
	
	public Bitmap grep(IBlur blur) {
    	long t = System.currentTimeMillis();
    	rootView.setDrawingCacheEnabled(true);
    	
    	Bitmap screen = rootView.getDrawingCache(true);
    	
    	if (bitmap!=null) {
    		bitmap.eraseColor(0x00FFFFFF);
    	} else {
    		bitmap = Bitmap.createBitmap(screen.getWidth()/factor, screen.getHeight()/factor, Bitmap.Config.ARGB_8888);
    	}

    	Canvas canvas = new Canvas(bitmap);
    	
    	Matrix m = new Matrix();
    	float scale = Math.min(bitmap.getWidth()/(float)screen.getWidth(), bitmap.getHeight()/(float)screen.getHeight());
    	m.postScale( scale, scale );
    	
    	canvas.drawBitmap(screen, m, null);
    	rootView.setDrawingCacheEnabled(false);
    	long e = System.currentTimeMillis();
    	Log.i("Timing", "Timed screencopy to " + (e-t) +"ms");
    	screenshot = e-t;
    	
    	t = System.currentTimeMillis();
    	Bitmap b = blur.filter((int)(kernel/factor), bitmap);
    	e = System.currentTimeMillis();
    	filtering = e-t;
    	
    	return b;
    } 
	
	
}
