package dungeongine.api.item;

import com.google.common.collect.Maps;
import dungeongine.apiimpl.item.ItemReference;

import java.util.Collections;
import java.util.Map;

public final class ItemStack {
	public static final ItemStack EMPTY = new ItemStack(null, 0, null);
	private final ItemReference item;
	private final int count;
	private final Map<String, Object> data;

	private ItemStack(Item item, int count, Map<String, Object> data) {
		this.item = item == null ? null : new ItemReference(item);
		this.count = count;
		this.data = Maps.newConcurrentMap();
		if (data != null)
			this.data.putAll(data);
	}

	public Item getItem() {
		return item == null ? null : item.getItem();
	}

	public int getCount() {
		return count;
	}

	public <T> T getData(String key) {
		return (T) data.get(key);
	}

	public <T> T setData(String key, T value) {
		return (T) data.put(key, value);
	}

	public Map<String, Object> serialize() {
		if (this == EMPTY)
			return Collections.emptyMap();
		Map<String, Object> data = Maps.newLinkedHashMap();
		data.put("item", item.getID());
		data.put("count", count);
		data.put("data", this.data);
		return data;
	}

	public static ItemStack unserialize(Map<String, Object> data) {
		if (!data.containsKey("item"))
			return EMPTY;
		return instance(Items.get((Long) data.get("item")), (Integer) data.get("count"), (Map<String, Object>) data.get("data"));
	}

	public static ItemStack instance(Item item) {
		return instance(item, 1);
	}

	public static ItemStack instance(Item item, int count) {
		return instance(item, count, null);
	}

	public static ItemStack instance(Item item, int count, Map<String, Object> data) {
		if (item == null)
			return EMPTY;
		if (count <= 0)
			return EMPTY;
		return new ItemStack(item, count, data);
	}
}
