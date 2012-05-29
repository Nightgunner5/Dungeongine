package dungeongine.apiimpl.map;

import com.google.common.base.Preconditions;
import dungeongine.api.entity.Entity;
import dungeongine.api.entity.Player;
import dungeongine.api.map.Location;
import dungeongine.api.map.Tile;
import dungeongine.api.map.World;

public class WorldImpl implements World {
	private final String name;

	public WorldImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Tile getTileAt(Location location) {
		Preconditions.checkArgument(location.getWorldName().equals(name));
		return getTileAt(location.getX(), location.getY());
	}

	@Override
	public Tile getTileAt(long x, long y) {
		return null;
	}

	@Override
	public Entity[] getEntities() {
		return new Entity[0];
	}

	@Override
	public Player[] getPlayers() {
		return new Player[0];
	}
}
