package droiddevs.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BlurredView extends FrameLayout implements DrawerListener {
	ScreenDumpUtils screenDumper;
	Bitmap current;
	float offset;
	int factor;
	ActionBarDrawerToggle toggler;
	IBlur blur;
	
	TextView d_method;
	TextView d_kernel;
	TextView d_downscale;
	TextView d_screenshot;
	TextView d_blur;
	TextView d_total;
	
	public BlurredView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setWillNotDraw(false);
	}

	public BlurredView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
	}

	public BlurredView(Context context) {
		super(context);
		setWillNotDraw(false);
	}
	

	@Override
	public void onDrawerStateChanged(int newState) {
		toggler.onDrawerStateChanged(newState);
	}
	
	@Override
	public void onDrawerSlide(View drawerView, float slideOffset) {
		offset = slideOffset;
		if (slideOffset==0) {
			current = null;	
		} else {
			invalidate();
		}
		toggler.onDrawerSlide(drawerView, slideOffset);
	}
	
	@Override
	public void onDrawerOpened(View drawerView) {
		toggler.onDrawerOpened(drawerView);
	}
	
	@Override
	public void onDrawerClosed(View drawerView) {
		toggler.onDrawerClosed(drawerView);
	}

	public void init(DrawerLayout drawer, ScreenDumpUtils screenDumper, int factor, ActionBarDrawerToggle toggler) {
		drawer.setDrawerListener(this);
		this.screenDumper = screenDumper;
		this.factor = factor;
		this.toggler = toggler;
		
		d_method = (TextView)drawer.findViewById(R.id.d_method);
		d_kernel  = (TextView)drawer.findViewById(R.id.d_kernel);
		d_downscale = (TextView)drawer.findViewById(R.id.d_downscale);
		d_screenshot = (TextView)drawer.findViewById(R.id.d_screenshot);
		d_blur = (TextView)drawer.findViewById(R.id.d_blur);
		d_total = (TextView)drawer.findViewById(R.id.d_total);
	}

	public void setBlur(String method, IBlur blur) {
		this.blur = blur;
		d_method.setText(method);
		current = null;
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (current==null) {
			current = screenDumper.grep(blur);
			//d_method.setText(this.wheel.getMethodName());
			d_kernel.setText("" + screenDumper.kernel);
			d_downscale.setText("" + screenDumper.factor);
			d_screenshot.setText( screenDumper.screenshot + "ms");
			d_blur.setText(screenDumper.filtering + "ms");
			d_total.setText((screenDumper.filtering + screenDumper.screenshot)+"ms");
		}
		if (current!=null) {
			int offsetX = (int)(getWidth()*(1-offset));
			canvas.save();
			canvas.translate(offsetX, 0);
			canvas.scale(screenDumper.factor, screenDumper.factor);
			canvas.drawBitmap(current, 0, 0,null);
			canvas.restore();
		}
	}
	
}
