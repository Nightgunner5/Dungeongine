package dungeongine.apiimpl.item;

import com.google.common.collect.Maps;
import dungeongine.api.item.Item;

import java.util.Map;

public class ItemImpl implements Item {
	public static Item unserialize(Map<String, Object> data) {
		return new ItemImpl();
	}

	public static Map<String, Object> serialize(Item item) {
		Map<String, Object> data = Maps.newLinkedHashMap();
		return data;
	}
}
