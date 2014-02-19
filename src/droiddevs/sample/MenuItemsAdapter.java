package droiddevs.sample;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MenuItemsAdapter extends BaseAdapter {

	
	private Activity context;


	public MenuItemsAdapter(Activity context) {
		this.context = context;
	}
	@Override
	public int getCount() {
		return 20;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView = context.getLayoutInflater().inflate(R.layout.menu_item, null);
			convertView.setTag(new ViewAccessor(convertView));
		}
		ViewAccessor a = (ViewAccessor)convertView.getTag();
		a.icon.setImageResource(R.drawable.ic_launcher);
		a.menuItem.setText("Menu item " + (position+1));
		return convertView;
	}

	
	static class ViewAccessor {
		TextView menuItem;
		ImageView icon;
		
		public ViewAccessor(View parent) {
			menuItem = (TextView)parent.findViewById(R.id.menu_item_text);
			icon = (ImageView)parent.findViewById(R.id.menu_item_icon);
		}
	}
}
