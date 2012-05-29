package dungeongine.api.map;

import dungeongine.api.entity.Entity;
import dungeongine.api.entity.Player;

public interface World {
	String getName();

	Tile getTileAt(Location location);

	Tile getTileAt(long x, long y);

	Entity[] getEntities();

	Player[] getPlayers();
}
