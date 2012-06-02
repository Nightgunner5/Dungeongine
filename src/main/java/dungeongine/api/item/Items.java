package dungeongine.api.item;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import dungeongine.apiimpl.BasicStorage;
import dungeongine.apiimpl.StorageImpl;
import dungeongine.apiimpl.item.*;

public final class Items {
	private Items() {
	}

	private static final BasicStorage<Long> nextID = new BasicStorage<Long>("next-item-id").setDefault(1L);
	private static LoadingCache<Long, ItemStorage> itemCache = CacheBuilder.newBuilder().softValues().build(new CacheLoader<Long, ItemStorage>() {
		@Override
		public ItemStorage load(Long key) throws Exception {
			return new ItemStorage(key, true);
		}
	});

	public static Item get(long id) {
		synchronized (nextID) {
			if (id < 1 || id >= nextID.getData())
				return null;
		}
		try {
			return itemCache.getUnchecked(id).getItem();
		} catch (RuntimeException ex) {
			return null;
		}
	}

	public static long getID(Item item) {
		DBObject data = StorageImpl.database.getCollection("items").findOne(new BasicDBObject().append("data", ItemImpl.serialize(item)));
		if (data != null)
			return Long.parseLong((String) data.get("identifier"), 16);
		return addItem(item);
	}

	private static long addItem(Item item) {
		long id;
		synchronized (nextID) {
			nextID.setData((id = nextID.getData()) + 1);
			nextID.save();
		}
		ItemStorage storage = new ItemStorage(id, false);
		storage.setItem(item);
		storage.save();
		return id;
	}

	public static <T extends Item> T create(Class<T> clazz) throws IllegalArgumentException {
		if (clazz == Item.class)
			return (T) new ItemImpl();
		if (clazz == Craftable.class)
			return (T) new CraftableImpl();
		if (clazz == Equippable.class)
			return (T) new EquippableImpl();
		if (clazz == CraftedEquippable.class)
			return (T) new CraftedEquippableImpl();
		throw new IllegalArgumentException("Unknown item type " + clazz.getName());
	}
}
