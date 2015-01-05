package net.wolfgangwerner.ibeacon.blinkenlist;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estimote.sdk.Region;

public class RegionAdapter extends ArrayAdapter<Region> {
	private Context context;
	private List<Region> values;

	public RegionAdapter(Context context, int resource, List<Region> objects) {
		super(context, resource, objects);
		this.context = context;
		this.values = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setText(values.get(position).getIdentifier());
		return label;
	}
		
	public int getPositionForRegion(Region region) {
		for (int i = 0; i < this.values.size(); i++) {
			if (values.get(i).getIdentifier().equals(region.getIdentifier()))
				return i;
		}
		return -1;
	}

}
