package dungeongine.apiimpl.item;

import dungeongine.api.item.Equippable;

public class EquippableImpl extends ItemImpl implements Equippable {
	private Slot slot;
	private boolean unique;

	@Override
	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	@Override
	public Slot getSlot() {
		return slot;
	}

	@Override
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	@Override
	public boolean isUnique() {
		return unique;
	}
}
