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

	public String getWorldName() {
		return world;
	}

	public long getX() {
		return x;
	}

	public long getY() {
		return y;
	}

	public Location add(Location other) throws IllegalArgumentException {
		checkWorld(other);
		return add(other.x, other.y);
	}

	public Location add(long x, long y) {
		return new Location(world, checkedAdd(this.x, x), checkedAdd(this.y, y));
	}

	public Location subtract(Location other) throws IllegalArgumentException {
		checkWorld(other);
		return subtract(other.x, other.y);
	}

	public Location subtract(long x, long y) {
		return new Location(world, checkedSubtract(this.x, x), checkedSubtract(this.y, y));
	}

	public long distanceSquared(Location other) {
		return subtract(other).lengthSquared();
	}

	public long lengthSquared() {
		return checkedAdd(checkedMultiply(x, x), checkedMultiply(y, y));
	}

	public long distance(Location other) {
		return LongMath.sqrt(distanceSquared(other), RoundingMode.HALF_UP);
	}

	public long length() {
		return LongMath.sqrt(lengthSquared(), RoundingMode.HALF_UP);
	}

	public World getWorld() {
		return Dungeongine.getWorld(world);
	}

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
