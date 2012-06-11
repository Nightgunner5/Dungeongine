package dungeongine.engine.item

import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

/**
 * Represents leaf nodes in an IDrop hierarchy.
 */
@TupleConstructor
@EqualsAndHashCode
class SingleDrop implements IDrop {
	Item item

	@Override
	List<Item> getItems(Random random) {
		List<Item> items = [item.clone() as Item]
		items.removeAll {it -> it == Item.NOTHING}
		items
	}

	@Override
	public String toString() {
		return item == Item.NOTHING ? "NOTHING" : item.toString()
	}
}
