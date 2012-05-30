package dungeongine.api.map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import dungeongine.api.Dungeongine;

import java.io.Serializable;
import java.math.RoundingMode;

import static com.google.common.math.LongMath.*;

/** Represents an immutable location in a world. */
public final class Location implements Serializable {
	private final String world;
	private final long x;
	private final long y;

	public Location(String world, long x, long y) {
		this.world = Preconditions.checkNotNull(world);
		this.x = x;
		this.y = y;
	}

	/** Gets the name of the world this location exists within. */
	public String getWorldName() {
		return world;
	}

	/** Gets the X coordinate of this location. */
	public long getX() {
		return x;
	}

	/** Gets the Y coordinate of this location. */
	public long getY() {
		return y;
	}

	/** Adds two locations, assuming they belong to the same world. */
	public Location add(Location other) throws IllegalArgumentException, ArithmeticException {
		checkWorld(other);
		return add(other.x, other.y);
	}

	/** Creates a new location representing a location (x, y) away from this location. */
	public Location add(long x, long y) throws ArithmeticException {
		return new Location(world, checkedAdd(this.x, x), checkedAdd(this.y, y));
	}

	/** Subtracts two locations, assuming they belong to the same world. */
	public Location subtract(Location other) throws IllegalArgumentException, ArithmeticException {
		checkWorld(other);
		return subtract(other.x, other.y);
	}

	/** Creates a new location representing a location (-x, -y) away from this location. */
	public Location subtract(long x, long y) throws ArithmeticException {
		return new Location(world, checkedSubtract(this.x, x), checkedSubtract(this.y, y));
	}

	/**
	 * Gets the distance squared between this and another location. This method is faster than distance() because it does
	 * not compute the square root.
	 */
	public long distanceSquared(Location other) throws ArithmeticException {
		return subtract(other).lengthSquared();
	}

	/**
	 * Gets the distance squared between this and (0, 0). This method is faster than length() because it does not compute
	 * the square root.
	 */
	public long lengthSquared() throws ArithmeticException {
		return checkedAdd(checkedMultiply(x, x), checkedMultiply(y, y));
	}

	/** Gets the distance between this and another location. Fractional values will be rounded half-up. */
	public long distance(Location other) throws ArithmeticException {
		return LongMath.sqrt(distanceSquared(other), RoundingMode.HALF_UP);
	}

	/** Gets the distance between this and (0, 0). Fractional values will be rounded half-up. */
	public long length() throws ArithmeticException {
		return LongMath.sqrt(lengthSquared(), RoundingMode.HALF_UP);
	}

	/** Shortcut for <code>Dungeongine.getWorld(location.getWorldName());</code> */
	public World getWorld() {
		return Dungeongine.getWorld(world);
	}

	/** Shortcut for <code>location.getWorld().getTileAt(location);</code> */
	public Tile getTile() {
		return getWorld().getTileAt(this);
	}

	private void checkWorld(Location other) throws IllegalArgumentException {
		Preconditions.checkNotNull(world);
		Preconditions.checkArgument(world.equals(other.world));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Location other = (Location) o;

		return x == other.x && y == other.y && Objects.equal(world, other.world);

	}

	@Override
	public int hashCode() {
		int result = world != null ? world.hashCode() : 0;
		result = 31 * result + (int) (x ^ (x >>> 32));
		result = 31 * result + (int) (y ^ (y >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s (%d, %d)", world, x, y);
	}
}
