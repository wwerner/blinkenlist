package net.wolfgangwerner.ibeacon.blinkenlist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.BeaconManager.MonitoringListener;
import com.estimote.sdk.Region;
import com.estimote.sdk.Beacon;

import net.wolfgangwerner.ibeacon.blinkenlist.model.Item;

public class BlinkenlistActivity extends FragmentActivity {

	private static String LOGTAG = "blinkenlist";
	private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	public static final Region REGION_NONE = new Region("<nicht erkannt>",
			ESTIMOTE_PROXIMITY_UUID, 999, 999);

	private static final Region REGION_BEER = new Region("Bier",
			ESTIMOTE_PROXIMITY_UUID, 2, 1);
	private static final Region REGION_SWEETS = new Region("Süßkram",
			ESTIMOTE_PROXIMITY_UUID, 3, 1);
	private static final Region REGION_VEG = new Region("Gemüse",
			ESTIMOTE_PROXIMITY_UUID, 1, 1);

	private ItemListAdapter itemListAdapter;
	private ListView shoppingListView;
	private List<Item> items;
	private List<Region> regions;

	private BeaconManager beaconManager;
	private Region currentRegion = REGION_NONE;

	private Spinner zoneSpinner = null;
	private RegionAdapter zoneSpinnerRegionAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		beaconManager = new BeaconManager(this);
		beaconManager.setBackgroundScanPeriod(1000, 250);
		beaconManager.setMonitoringListener(new MonitoringListener() {
			@Override
			public void onEnteredRegion(com.estimote.sdk.Region region, java.util.List<Beacon> list) {
				Log.i(LOGTAG + ".zones", "Entered " + region);

				int position = zoneSpinnerRegionAdapter
						.getPositionForRegion(region);
				zoneSpinner.setSelection(position);

				itemListAdapter.setRegion(region);
				itemListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onExitedRegion(com.estimote.sdk.Region region) {
				Log.i(LOGTAG + ".zones", "Exited " + region);
				zoneSpinner.setSelection(0);
				itemListAdapter.setRegion(REGION_NONE);
				itemListAdapter.notifyDataSetChanged();
			}
		});

		setContentView(R.layout.activity_shopping_list);

		shoppingListView = (ListView) findViewById(R.id.shopping_list);

		regions = setupRegions();
		items = createDummyItems();

		itemListAdapter = new ItemListAdapter(this,
				R.layout.fragment_shopping_list_row, items);
		shoppingListView.setAdapter(itemListAdapter);

		this.zoneSpinner = (Spinner) findViewById(R.id.content_zone);
		this.zoneSpinnerRegionAdapter = new RegionAdapter(this,
				R.layout.fragment_zone_spinner_row, regions);
		this.zoneSpinner.setAdapter(zoneSpinnerRegionAdapter);
		this.zoneSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentView,
							View selectedItemView, int position, long id) {
						updateRegion((Region) zoneSpinner
								.getItemAtPosition(position));
					}

					@Override
					public void onNothingSelected(AdapterView<?> parentView) {
						// updateRegion(DEFAULT_REGION);
					}

					private void updateRegion(Region region) {
						itemListAdapter.setRegion(region);
						itemListAdapter.notifyDataSetChanged();

					}
				});
	}

	@Override
	protected void onStart() {
		super.onStart();
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(REGION_BEER);
					beaconManager.startMonitoring(REGION_VEG);
					beaconManager.startMonitoring(REGION_SWEETS);
					Log.i(LOGTAG, "Started monitoring.");
				} catch (RemoteException e) {
					Log.e(LOGTAG, "Cannot start monitoring", e);
				}
			}
		});
	}

	@Override
	protected void onStop() {
		try {
			beaconManager.stopMonitoring(REGION_BEER);
			beaconManager.stopMonitoring(REGION_VEG);
			beaconManager.stopMonitoring(REGION_SWEETS);
		} catch (RemoteException e) {
			Log.e(LOGTAG, "Cannot stop but it does not matter now", e);
		}

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		beaconManager.disconnect();
		super.onDestroy();
	}

	private List<Item> createDummyItems() {
		List<Item> items = new ArrayList<Item>();

		items.add(Item.from("Gurke", REGION_VEG));
		items.add(Item.from("Grüner August", REGION_BEER));
		items.add(Item.from("Ritter Sport Knusperflakes", REGION_SWEETS));
		items.add(Item.from("Apfel", REGION_VEG));
		items.add(Item.from("Zwickl Max", REGION_BEER));
		items.add(Item.from("Snickers", REGION_SWEETS));
		items.add(Item.from("Kartoffel", REGION_VEG));

		Log.i(LOGTAG, "Initialized items.");
		return items;
	}

	private List<Region> setupRegions() {
		List<Region> regions = new ArrayList<Region>();
		regions.add(REGION_NONE);
		regions.add(REGION_VEG);
		regions.add(REGION_SWEETS);
		regions.add(REGION_BEER);

		return regions;
	}
}
