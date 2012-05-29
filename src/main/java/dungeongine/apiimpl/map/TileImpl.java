package dungeongine.apiimpl.map;

import dungeongine.api.map.Location;
import dungeongine.api.map.Tile;
import dungeongine.api.map.World;

import java.io.Serializable;

public class TileImpl implements Tile, Serializable {
	private final Location location;
	private boolean passable;

	public TileImpl(Location location, boolean passable) {
		this.location = location;
		this.passable = passable;
	}

	@Override
	public void setPassable(boolean passable) {
		this.passable = passable;
	}

	@Override
	public boolean isPassable() {
		return passable;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public World getWorld() {
		return location.getWorld();
	}
}
