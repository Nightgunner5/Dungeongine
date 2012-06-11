package dungeongine.engine.item

import groovy.transform.EqualsAndHashCode
import dungeongine.engine.Utils

/**
 * A table of drops.
 *
 * <ul>
 *     <li>0.2 -> 20% drop rate</li>
 *     <li>2.5 -> 50% drop rate + 2 bonus drops</li>
 *     <li>7.0 -> 7 drops</li>
 * </ul>
 */
@EqualsAndHashCode
class DropTable implements IDrop {
	final List dropRates = []

	@Override
	List<Item> getItems(Random random) {
		List<Item> items = []
		dropRates.each {
			def (item, rate) = it
			int guaranteed = rate
			rate -= guaranteed
			guaranteed.times {
				items.addAll(item.getItems(random))
			}
			if (rate > random.nextDouble()) {
				items.addAll(item.getItems(random))
			}
		}
		items.removeAll {it -> it == Item.NOTHING}
		items
	}

	DropTable call(List list) {
		dropRates.addAll(list)
		this
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder('DropTable {\n')
		dropRates.each {
			def (item, rate) = it
			sb.append('    ').append(rate).append(': ').append(Utils.indent(item.toString())).append('\n')
		}
		sb.append('}')
	}
}
