package dungeongine.engine.item

import groovy.transform.AutoClone
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

@TupleConstructor
@EqualsAndHashCode
@AutoClone
class Item {
	public static final Item NOTHING = new Item(null)

	String name

	@Override
	public String toString() {
		"Item($name)"
	}
}
