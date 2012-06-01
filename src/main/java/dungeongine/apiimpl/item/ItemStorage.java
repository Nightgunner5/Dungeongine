package dungeongine.apiimpl.item;

import dungeongine.api.item.Item;
import dungeongine.apiimpl.BasicStorage;

import java.util.Map;

public class ItemStorage extends BasicStorage<Map<String, Object>> {
	public ItemStorage(long id, boolean requireExists) throws IllegalArgumentException {
		super("items", Long.toHexString(id), requireExists);
	}

	public void setItem(Item item) {
		setData(ItemImpl.serialize(item));
	}

	public Item getItem() {
		return ItemImpl.unserialize(getData());
	}
}
