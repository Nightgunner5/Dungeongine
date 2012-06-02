package dungeongine.api.item;

import dungeongine.api.StatType;

public interface Equippable extends Item {
	/** Sets the {@link Slot} the item is equippable in. */
	void setSlot(Slot slot);

	/** Gets the {@link Slot} the item is equippable in. */
	Slot getSlot();

	/** Sets the unique status of this item. Unique items cannot be equipped with other copies of the same item. */
	void setUnique(boolean unique);

	/** Gets the unique status of this item. Unique items cannot be equipped with other copies of the same item. */
	boolean isUnique();

	/**
	 * Gets the primary stat type. This is usually {@link StatType#DEXTERITY dexterity}, {@link StatType#FORCE force}, or
	 * {@link StatType#WISDOM  wisdom}.
	 */
	StatType getPrimaryStat();

	/**
	 * Sets the primary stat type. This is usually {@link StatType#DEXTERITY dexterity}, {@link StatType#FORCE force}, or
	 * {@link StatType#WISDOM  wisdom}.
	 */
	void setPrimaryStat(StatType primaryStat);

	/**
	 * Gets the secondary stat type. This is usually one of the {@link StatType}s other than the three {@link
	 * #getPrimaryStat() primaries}.
	 */
	StatType getSecondaryStat();

	/**
	 * Sets the secondary stat type. This is usually one of the {@link StatType}s other than the three {@link
	 * #getPrimaryStat() primaries}.
	 */
	void setSecondaryStat(StatType secondaryStat);

	/**
	 * Gets the tertiary stat type (epic items have two secondary stats). This is usually one of the {@link StatType}s
	 * other than the three {@link #getPrimaryStat() primaries}.
	 */
	StatType getTertiaryStat();

	/**
	 * Sets the tertiary stat type (epic items have two secondary stats) . This is usually one of the {@link StatType}s
	 * other than the three {@link #getPrimaryStat() primaries}.
	 */
	void setTertiaryStat(StatType tertiaryStat);

	/**
	 * A slot for an equippable item.
	 * <p/>
	 * There are ten {@link #FINGER} slots, corresponding to the fingers on human hands.
	 */
	public static enum Slot {
		/** Helmets, hats, faceguards */
		HEAD,
		/** Shoulder pads */
		SHOULDERS,
		/** Capes, shields, backpacks */
		BACK,
		/** Necklaces, brooches, amulets */
		NECK,
		/** Shirts, breastplates, top half of armor/robe */
		TORSO,
		/** Bracers */
		WRISTS,
		/** Gloves, mittens */
		HANDS,
		/** Belts */
		WAIST,
		/** Pants, kilts, bottom half of armor/robe */
		LEGS,
		/** Boots, shoes */
		FEET,
		/** Rings */
		FINGER
	}
}
