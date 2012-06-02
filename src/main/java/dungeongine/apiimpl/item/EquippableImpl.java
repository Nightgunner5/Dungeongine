package dungeongine.apiimpl.item;

import dungeongine.api.StatType;
import dungeongine.api.item.Equippable;

public class EquippableImpl extends ItemImpl implements Equippable {
	private Slot slot;
	private boolean unique;
	private StatType primaryStat;
	private StatType secondaryStat;
	private StatType tertiaryStat;

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

	@Override
	public StatType getPrimaryStat() {
		return primaryStat;
	}

	@Override
	public void setPrimaryStat(StatType primaryStat) {
		this.primaryStat = primaryStat;
	}

	@Override
	public StatType getSecondaryStat() {
		return secondaryStat;
	}

	@Override
	public void setSecondaryStat(StatType secondaryStat) {
		this.secondaryStat = secondaryStat;
	}

	@Override
	public StatType getTertiaryStat() {
		return tertiaryStat;
	}

	@Override
	public void setTertiaryStat(StatType tertiaryStat) {
		this.tertiaryStat = tertiaryStat;
	}
}
