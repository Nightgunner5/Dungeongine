package dungeongine.api.item;

public interface Item {
	int getLevel();

	void setLevel(int level);

	String getName();

	void setName(String name);

	Quality getQuality();

	void setQuality(Quality quality);

	public static enum Quality {
		/**
		 * The least potent item quality -- below tier 1.
		 * <p/>
		 * Equipment: No stats.
		 * <p/>
		 * Craftables: Nonexistent.
		 * <p/>
		 * Others: Worthless junk.
		 */
		POOR,
		/**
		 * The first tier of potency.
		 * <p/>
		 * Equipment: One primary stat.
		 * <p/>
		 * Craftables: 1-3 reagents.
		 * <p/>
		 * Others: Basic everyday items like glass bottles and planks.
		 */
		UNCOMMON,
		/**
		 * The second tier of potency.
		 * <p/>
		 * Equipment: One primary, one secondary stat.
		 * <p/>
		 * Craftables: 3-5 reagents.
		 * <p/>
		 * Others: Not common, but not one-of-a-kind either. Dragon scales are a good example.
		 */
		RARE,
		/**
		 * The third tier of potency.
		 * <p/>
		 * Equipment: One primary, two secondary stats.
		 * <p/>
		 * Craftables: 5-10 reagents.
		 * <p/>
		 * Others: Rare items including crystalized elements, spheres of entropy, and rare hides.
		 */
		EPIC,
		/**
		 * The most potent items in the game. Items with custom code.
		 * <p/>
		 * Equipment: One primary, two secondary, and custom effect per-item.
		 * <p/>
		 * Craftables: Similar to epic, but with one or more legendary reagent.
		 * <p/>
		 * Others: Extremely rare drops.
		 */
		LEGENDARY;

		public String getName() {
			switch (this) {
				case POOR:
					return "Poor";
				case UNCOMMON:
					return "Uncommon";
				case RARE:
					return "Rare";
				case EPIC:
					return "Epic";
				case LEGENDARY:
					return "Legendary";
			}
			throw new IllegalStateException();
		}
	}
}
