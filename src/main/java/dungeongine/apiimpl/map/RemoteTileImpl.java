package dungeongine.apiimpl.map;

import dungeongine.api.map.Location;
import dungeongine.api.map.Tile;
import dungeongine.api.map.World;

public class RemoteTileImpl implements Tile {
	private final Location location;
	private boolean passable;

	public RemoteTileImpl(Location location) {
		this.location = location;
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

	@Override
	public void save() {
		// Don't save since this isn't our object.
	}
}
