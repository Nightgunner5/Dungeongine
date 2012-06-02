package dungeongine.apiimpl.item;

import dungeongine.api.StatType;
import dungeongine.api.item.Craftable;
import dungeongine.api.item.CraftedEquippable;
import dungeongine.api.item.Equippable;
import dungeongine.api.item.Item;

public class CraftedEquippableImpl extends ItemImpl implements CraftedEquippable {
	private final Craftable craftable = new CraftableImpl();
	private final Equippable equippable = new EquippableImpl();
	private int level;

	@Override
	public Item[] getReagents() {
		return craftable.getReagents();
	}

	@Override
	public void setReagents(Item[] reagents) {
		craftable.setReagents(reagents);
	}

	@Override
	public void setSlot(Slot slot) {
		equippable.setSlot(slot);
	}

	@Override
	public Slot getSlot() {
		return equippable.getSlot();
	}

	@Override
	public void setUnique(boolean unique) {
		equippable.setUnique(unique);
	}

	@Override
	public boolean isUnique() {
		return equippable.isUnique();
	}

	@Override
	public StatType getPrimaryStat() {
		return equippable.getPrimaryStat();
	}

	@Override
	public void setPrimaryStat(StatType primaryStat) {
		equippable.setPrimaryStat(primaryStat);
	}

	@Override
	public StatType getSecondaryStat() {
		return equippable.getSecondaryStat();
	}

	@Override
	public void setSecondaryStat(StatType secondaryStat) {
		equippable.setSecondaryStat(secondaryStat);
	}

	@Override
	public StatType getTertiaryStat() {
		return equippable.getTertiaryStat();
	}

	@Override
	public void setTertiaryStat(StatType tertiaryStat) {
		equippable.setTertiaryStat(tertiaryStat);
	}
}
