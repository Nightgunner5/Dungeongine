package dungeongine.engine

import groovy.transform.EqualsAndHashCode

/**
 * Represents a location on the map. Basic math operations are supported, such as addition, subtraction, and multiplying
 * by integers.
 */
@EqualsAndHashCode
class Location {
	/**
	 * The origin of the map (0, 0).
	 */
	public static final Location ORIGIN = new Location(0, 0)

	final long x, y

	/**
	 * Constructs a new Location (x, y).
	 */
	Location(long x, long y) {
		this.x = x
		this.y = y
	}

	/**
	 * Adds a given Location's coordinates to this Location's coordinates.
	 */
	Location plus(Location other) {
		new Location(x + other.x, y + other.y)
	}

	/**
	 * Subtracts a given Location's coordinates from this Location's coordinates.
	 */
	Location minus(Location other) {
		new Location(x - other.x, y - other.y)
	}

	/**
	 * Multiplies both coordinates by a given scale.
	 */
	Location multiply(long scale) {
		new Location(x * scale, y * scale)
	}

	/**
	 * Multiplies both coordinates by -1.
	 */
	Location negative() {
		new Location(-x, -y)
	}

	/**
	 * Gets the distance from the origin.
	 */
	double getLength() {
		Math.hypot(x, y)
	}

	/**
	 * Gets the distance from a given Location.
	 */
	double distance(Location other) {
		(this - other).length
	}

	@Override
	public String toString() {
		"($x, $y)"
	}
}
