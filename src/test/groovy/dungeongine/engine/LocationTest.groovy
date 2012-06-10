package dungeongine.engine

@Newify([Location])
class LocationTest extends GroovyTestCase {
	void testEquality() {
		assert Location(1, 1) == Location(1, 1)
		assert Location(1, 1) != Location(1, 2)
	}

	void testAdd() {
		assert Location(1, 0) + Location(0, 1) == Location(1, 1)
	}

	void testSubtract() {
		assert Location(1, 2) - Location(0, 1) == Location(1, 1)
	}

	void testScale() {
		assert Location(1, 2) * 5 == Location(5, 10)
	}

	void testNegate() {
		assert -Location(1, 2) == Location(-1, -2)
	}

	void testLength() {
		assert Location(3, 4).length == 5
	}

	void testDistance() {
		assert Location(3, 5).distance(Location(0, 1)) == 5
	}
}
