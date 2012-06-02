package dungeongine.apiimpl.item;

import dungeongine.api.item.Craftable;
import dungeongine.api.item.Item;

public class CraftableImpl extends ItemImpl implements Craftable {
	private ItemReference[] reagents = {};

	@Override
	public Item[] getReagents() {
		Item[] reagents = new Item[this.reagents.length];
		for (int i = 0; i < reagents.length; i++)
			reagents[i] = this.reagents[i].getItem();
		return reagents;
	}

	@Override
	public void setReagents(Item... reagents) {
		this.reagents = new ItemReference[reagents.length];
		for (int i = 0; i < reagents.length; i++)
			this.reagents[i] = new ItemReference(reagents[i]);
	}
}
