package net.wolfgangwerner.ibeacon.blinkenlist.model;

import com.estimote.sdk.Region;

public class Item {
	private String title;
	private Region region;

	private Item(){}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public static Item from(String title, Region region) {
		Item item = new Item();
		item.setTitle(title);
		item.setRegion(region);
		return item;
	}
}
