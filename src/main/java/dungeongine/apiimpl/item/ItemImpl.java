package dungeongine.apiimpl.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dungeongine.api.StatType;
import dungeongine.api.item.*;

import java.util.List;
import java.util.Map;

public class ItemImpl implements Item {
	private int level;
	private String name;
	private Quality quality;

	public static Item unserialize(Map<String, Object> data) {
		Item item;
		switch ((String) data.get("type")) {
			case "craftedequippable":
				item = new CraftedEquippableImpl();
				break;
			case "equippable":
				item = new EquippableImpl();
				break;
			case "craftable":
				item = new CraftableImpl();
				break;
			case "item":
				item = new ItemImpl();
				break;
			default:
				throw new IllegalStateException("Unknown type: " + data.get("type"));
		}
		item.setName((String) data.get("name"));
		item.setLevel((Integer) data.get("level"));
		item.setQuality(Quality.valueOf((String) data.get("quality")));
		if (item instanceof Craftable) {
			List<Long> reagents = (List<Long>) data.get("reagents");
			Item[] parsed = new Item[reagents.size()];
			int i = 0;
			for (Long reagent : reagents) {
				parsed[i++] = Items.get(reagent);
			}
			((Craftable) item).setReagents(parsed);
		}
		if (item instanceof Equippable) {
			((Equippable) item).setSlot(Equippable.Slot.valueOf((String) data.get("slot")));
			((Equippable) item).setUnique((Boolean) data.get("unique"));
			((Equippable) item).setPrimaryStat(StatType.valueOf((String) data.get("primaryStat")));
			((Equippable) item).setSecondaryStat(StatType.valueOf((String) data.get("secondaryStat")));
			((Equippable) item).setTertiaryStat(StatType.valueOf((String) data.get("tertiaryStat")));
		}
		return item;
	}

	public static Map<String, Object> serialize(Item item) {
		Map<String, Object> data = Maps.newLinkedHashMap();
		data.put("type", "item");
		data.put("name", item.getName());
		data.put("level", item.getLevel());
		data.put("quality", item.getQuality().name());
		if (item instanceof Craftable) {
			data.put("type", "craftable");
			List<Long> reagents = Lists.newArrayList();
			for (Item reagent : ((Craftable) item).getReagents()) {
				reagents.add(Items.getID(reagent));
			}
			data.put("reagents", reagents);
		}
		if (item instanceof Equippable) {
			data.put("type", "equippable");
			data.put("slot", ((Equippable) item).getSlot().name());
			data.put("unique", ((Equippable) item).isUnique());
			data.put("primaryStat", ((Equippable) item).getPrimaryStat().name());
			data.put("secondaryStat", ((Equippable) item).getSecondaryStat().name());
			data.put("tertiaryStat", ((Equippable) item).getTertiaryStat().name());
		}
		if (item instanceof CraftedEquippable) {
			data.put("type", "craftedequippable");
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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Quality getQuality() {
		return quality;
	}

	@Override
	public void setQuality(Quality quality) {
		this.quality = quality;
	}
}
