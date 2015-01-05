package net.wolfgangwerner.ibeacon.blinkenlist;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estimote.sdk.Region;

import net.wolfgangwerner.ibeacon.blinkenlist.model.Item;

public class ItemListAdapter extends ArrayAdapter<Item> {
    private LayoutInflater inflater;
    private static String LOGTAG = "blinkenlist.listadapter";

    private final List<Item> items;
    private Region currentRegion;

    public ItemListAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        this.items = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.fragment_shopping_list_row,
                parent, false);
        TextView firstLine = (TextView) rowView
                .findViewById(R.id.shopping_list_first_line);
        TextView secondLine = (TextView) rowView
                .findViewById(R.id.shopping_list_second_line);

        Region region = this.items.get(position).getRegion();
        firstLine.setText(this.items.get(position).getTitle());
        secondLine.setText(region.getIdentifier());

        if (region == null || currentRegion == null) {
            return rowView;
        }

        String regionId = region.getIdentifier();
        String currentRegionId = currentRegion.getIdentifier();

        if (regionId.equals(currentRegionId)) {
            firstLine.setBackgroundColor(Color.YELLOW);
            secondLine.setBackgroundColor(Color.YELLOW);
        }

        return rowView;
    }

    public void setRegion(Region region) {
        this.currentRegion = region;
    }
}
