package dungeongine.engine.item

import dungeongine.engine.Utils
import groovy.transform.EqualsAndHashCode

/**
 * A carnival prize wheel of drops. Guaranteed to return exactly one drop.
 */
@EqualsAndHashCode
class DropWheel implements IDrop {
	final List dropRates = []

	@Override
	List<Item> getItems(Random random) {
		double total = 0
		dropRates.each {total += it[1]}
		double index = random.nextDouble() * total
		List<Item> items = []
		dropRates.each {
			def (item, rate) = it
			if (index >= 0)
				items = item.getItems(random)
			index -= rate
		}
		items.removeAll {it -> it == Item.NOTHING}
		items
	}

	DropWheel call(List list) {
		dropRates.addAll(list)
		this
	}

	@Override
	public String toString() {
		double total = 0
		dropRates.each {total += it[1]}

		StringBuilder sb = new StringBuilder('DropWheel {\n')
		dropRates.each {
			def (item, rate) = it
			sb.append('    ').append(Math.round(rate * 1000 / total) / 10.0).append('%: ').append(Utils.indent(item.toString())).append('\n')
		}
		sb.append('}')
	}
}
