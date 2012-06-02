package dungeongine.bootstrap.database;

import dungeongine.api.item.Craftable;
import dungeongine.api.item.Item;
import dungeongine.api.item.Items;

import static dungeongine.api.item.Item.Quality.UNCOMMON;

public final class ItemDB {
	private ItemDB() {
	}

	private static Item[] get(long... id) {
		Item[] items = new Item[id.length];
		for (int i = 0; i < id.length; i++)
			items[i] = Items.get(id[i]);
		return items;
	}

	static long item(String name, int level, Item.Quality quality) {
		Item item = Items.create(Item.class);
		item.setName(name);
		item.setLevel(level);
		item.setQuality(quality);
		return Items.getID(item);
	}

	static long craftable(String name, int level, Item.Quality quality, long... reagents) {
		Craftable item = Items.create(Craftable.class);
		item.setName(name);
		item.setLevel(level);
		item.setQuality(quality);
		item.setReagents(get(reagents));
		return Items.getID(item);
	}

	public static final class Materials {
		private Materials() {
		}

		public static final long LIGHT_CLOTH = item("Light Cloth", 5, UNCOMMON);
		public static final long MEDIUM_CLOTH = craftable("Medium Cloth", 15, UNCOMMON, LIGHT_CLOTH, LIGHT_CLOTH, LIGHT_CLOTH);
		public static final long HEAVY_CLOTH = craftable("Heavy Cloth", 25, UNCOMMON, MEDIUM_CLOTH, MEDIUM_CLOTH, MEDIUM_CLOTH);

		public static final long LIGHT_LEATHER = item("Light Leather", 5, UNCOMMON);
		public static final long MEDIUM_LEATHER = craftable("Medium Leather", 15, UNCOMMON, LIGHT_LEATHER, LIGHT_LEATHER, LIGHT_LEATHER);
		public static final long HEAVY_LEATHER = craftable("Heavy Leather", 25, UNCOMMON, MEDIUM_LEATHER, MEDIUM_LEATHER, MEDIUM_LEATHER);
	}
}
