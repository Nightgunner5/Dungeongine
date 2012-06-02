package dungeongine.api;

/**
 * Each primary {@link StatType} has an offensive, a defensive, and a noncombat bonus of equal strength. Secondaries have
 * one or two of the three types of effects, and if there is a second, it is much weaker than the first.
 */
public enum StatType {
	/** Primary: physical attack speed, dodge chance, movement speed */
	DEXTERITY,
	/** Primary: physical attack damage, health, endurance */
	FORCE,
	/** Primary: magical attack damage, magical resistance, healing effectiveness */
	WISDOM,

	/** Secondary: critical hit chance, dodge chance */
	CUNNING,
	/** Secondary: physical/magical attack speed, resource regeneration */
	HASTE,
	/** Secondary: physical/magical attack damage */
	POWER,
	/** Secondary: health, endurance */
	STAMINA,
	/** Secondary: chance of finding more/better loot */
	WEALTH,

	;

	public boolean isPrimary() {
		switch (this) {
			case DEXTERITY:
			case FORCE:
			case WISDOM:
				return true;
		}
		return false;
	}

	public boolean isSecondary() {
		return !isPrimary();
	}
}
