package dungeongine.apiimpl.item;

import dungeongine.api.item.Craftable;
import dungeongine.api.item.Item;

import java.util.Arrays;

public class CraftableImpl extends ItemImpl implements Craftable {
	private ItemReference[] reagents = {};

	@Override
	public Item[] getReagents() {
		return ItemReference.get(reagents);
	}

	@Override
	public void setReagents(Item... reagents) {
		this.reagents = ItemReference.get(reagents);
		Arrays.sort(this.reagents);
	}
}
