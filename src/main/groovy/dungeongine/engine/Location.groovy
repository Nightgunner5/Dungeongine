package dungeongine.engine

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Location {
	public static final Location ORIGIN = new Location(0, 0)

	final long x, y

	Location(long x, long y) {
		this.x = x
		this.y = y
	}

	def plus(Location other) {
		new Location(x + other.x, y + other.y)
	}

	def minus(Location other) {
		new Location(x - other.x, y - other.y)
	}

	def multiply(long scale) {
		new Location(x * scale, y * scale)
	}

	def negative() {
		new Location(-x, -y)
	}

	def getLength() {
		Math.hypot(x, y)
	}

	def distance(Location other) {
		(this - other).length
	}

	@Override
	public String toString() {
		"($x, $y)"
	}
}
