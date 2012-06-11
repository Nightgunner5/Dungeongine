package dungeongine.item

import dungeongine.engine.item.DropTable
import dungeongine.engine.item.DropWheel
import dungeongine.engine.item.Item
import dungeongine.engine.item.SingleDrop

class DropTest extends GroovyTestCase {
	void testLeaf() {
		Item item = new Item("Test of Testing")
		assert new SingleDrop(item).getItems(new Random()) == [item]
	}

	void testWheel() {
		Item item = new Item("Test of Testing")
		Item item2 = new Item("Fail of Failing")
		assert new DropWheel()([
				[new SingleDrop(item), 2.5],
				[new SingleDrop(item2), 0.0]
		]).getItems(new Random()) == [item]
	}

	void testTable() {
		Item item = new Item("Test of Testing")
		Item item2 = new Item("Success of Succeeding")
		Item item3 = new Item("Fail of Failing")
		assert new DropTable()([
				[new SingleDrop(item), 2.0],
				[new SingleDrop(item2), 1.0],
				[new SingleDrop(item3), 0.0]
		]).getItems(new Random()) == [item, item, item2]
	}

	void testNothing() {
		assert new DropTable()([
				[new SingleDrop(Item.NOTHING), 5.0]
		]).getItems(new Random()) == []
	}
}
