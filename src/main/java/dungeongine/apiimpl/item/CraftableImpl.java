package dungeongine.apiimpl.item;

import dungeongine.api.item.Craftable;
import dungeongine.api.item.Item;

public class CraftableImpl extends ItemImpl implements Craftable {
	private Item[] reagents;

	@Override
	public Item[] getReagents() {
		return reagents.clone();
	}

	@Override
	public void setReagents(Item[] reagents) {
		this.reagents = reagents.clone();
	}
}
