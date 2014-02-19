package droiddevs.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class Main extends Activity {
	
	private DrawerLayout drawer; 
	private ScreenDumpUtils screenDumper;
	private ActionBarDrawerToggle toggler;
	private SeekBar kernel;
	private SeekBar downscale;
	private TextView text_kernel;
	private TextView text_downscale;
	private BlurredView blurredView;
	
	
	public void setKernelValue(int value) {
		text_kernel.setText("Kernel ("+value+")");
		screenDumper.setKernel(value);
	}
	
	public void setDownscaleValue(int value) {
		text_downscale.setText("Downscale ("+(value+1)+")");
		screenDumper.setFactor(1+value);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		kernel = (SeekBar)findViewById(R.id.kernel);
		downscale = (SeekBar)findViewById(R.id.downscale);
		text_kernel = (TextView)findViewById(R.id.text_kernel);
		text_downscale = (TextView)findViewById(R.id.text_downscale);
		
		kernel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					setKernelValue(progress);
					
				}
			}
		});
		
		downscale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					setDownscaleValue(progress);
					
					
				}
			}
		});
		drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		screenDumper = new ScreenDumpUtils(this, drawer.getChildAt(0), 8);
		
		setKernelValue(kernel.getProgress());
		setDownscaleValue(kernel.getProgress());
		
		
		
		toggler = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_drawer, R.string.open_drawer, R.string.close_drawer);
		blurredView = (BlurredView)drawer.getChildAt(1);
		
		blurredView.init(drawer, screenDumper, 8, toggler);
	
		//ListView list = (ListView)findViewById(R.id.items);
		//list.setAdapter(new MenuItemsAdapter(this));
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		findViewById(R.id.intrensic).setOnClickListener(new View.OnClickListener() {
			IBlur intrensic = new IntrinsicBlur(Main.this);
			@Override
			public void onClick(View v) {
				blurredView.setBlur("Intrensic", intrensic);
				drawer.openDrawer(Gravity.LEFT);
			}
		});
		
		findViewById(R.id.renderscript).setOnClickListener(new View.OnClickListener() {
			IBlur renderscript = new RSBlur(Main.this);
			@Override
			public void onClick(View v) {
				blurredView.setBlur("RenderScript", renderscript);
				drawer.openDrawer(Gravity.LEFT);
			}
		});
		
		findViewById(R.id.cpu).setOnClickListener(new View.OnClickListener() {
			IBlur cpu = new IntrinsicBlur(Main.this);
			@Override
			public void onClick(View v) {
				blurredView.setBlur("CPU", cpu);
				drawer.openDrawer(Gravity.LEFT);
			}
		});
		
		findViewById(R.id.threaded).setOnClickListener(new View.OnClickListener() {
			IBlur threaded = new IntrinsicBlur(Main.this);
			@Override
			public void onClick(View v) {
				blurredView.setBlur("Threaded", threaded);
				drawer.openDrawer(Gravity.LEFT);
			}
		});
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggler.syncState();
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if (toggler.onOptionsItemSelected(item)) {
			return true;
        }
		return super.onOptionsItemSelected(item);
    }
	
	public void onSyncButtonPressed() {
		drawer.openDrawer(Gravity.LEFT);
	}

	public void onLoginButtonPressed() {
		drawer.openDrawer(Gravity.LEFT);
	}

	public void onStopButtonPressed() {
		drawer.openDrawer(Gravity.LEFT);
	}
}
