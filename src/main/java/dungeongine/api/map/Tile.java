package dungeongine.api.map;

import dungeongine.api.Storage;

public interface Tile extends Storage {
	void setPassable(boolean passable);

	boolean isPassable();

	Location getLocation();

	World getWorld();
}
