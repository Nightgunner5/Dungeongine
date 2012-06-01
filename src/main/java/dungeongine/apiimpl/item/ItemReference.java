package dungeongine.apiimpl.item;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import dungeongine.api.item.Item;
import dungeongine.api.item.Items;

public class ItemReference implements Comparable<ItemReference> {
	private final long id;

	public ItemReference(Item item) {
		id = Items.getID(item);
	}

	public Item getItem() {
		return Items.get(id);
	}

	public long getID() {
		return id;
	}

	@Override
	public int compareTo(ItemReference o) {
		return Ints.saturatedCast(id - o.id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemReference that = (ItemReference) o;

		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Longs.hashCode(id);
	}
}
