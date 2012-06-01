package dungeongine.apiimpl.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dungeongine.api.item.Craftable;
import dungeongine.api.item.Item;

import java.util.List;
import java.util.Map;

public class ItemImpl implements Item {
	private int level;

	public static Item unserialize(Map<String, Object> data) {
		Item item;
		switch ((String) data.get("type")) {
			case "craftable":
				item = new CraftableImpl();
				break;
			case "item":
				item = new ItemImpl();
				break;
			default:
				throw new IllegalStateException("Unknown type: " + data.get("type"));
		}
		item.setLevel((Integer) data.get("level"));
		if (data.containsKey("reagents")) {
			List<Map<String, Object>> reagents = (List<Map<String, Object>>) data.get("reagents");
			Item[] parsed = new Item[reagents.size()];
			int i = 0;
			for (Map<String, Object> reagent : reagents) {
				parsed[i++] = unserialize(reagent);
			}
			((Craftable) item).setReagents(parsed);
		}
		return item;
	}

	public static Map<String, Object> serialize(Item item) {
		Map<String, Object> data = Maps.newLinkedHashMap();
		data.put("type", "item");
		data.put("level", item.getLevel());
		if (item instanceof Craftable) {
			data.put("type", "craftable");
			List<Map<String, Object>> reagents = Lists.newArrayList();
			for (Item reagent : ((Craftable) item).getReagents()) {
				reagents.add(serialize(reagent));
			}
			data.put("reagents", reagents);
		}
		return data;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}
}
