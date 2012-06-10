package dungeongine.engine

class LocationTest extends GroovyTestCase {
	void testEquality() {
		assert new Location(1, 1) == new Location(1, 1)
		assert new Location(1, 1) != new Location(1, 2)
	}

	void testAdd() {
		assert new Location(1, 0) + new Location(0, 1) == new Location(1, 1)
	}

	void testSubtract() {
		assert new Location(1, 2) - new Location(0, 1) == new Location(1, 1)
	}

	void testScale() {
		assert new Location(1, 2) * 5 == new Location(5, 10)
	}

	void testNegate() {
		assert -new Location(1, 2) == new Location(-1, -2)
	}

	void testLength() {
		assert new Location(3, 4).length == 5
	}

	void testDistance() {
		assert new Location(3, 5).distance(new Location(0, 1)) == 5
	}
}
