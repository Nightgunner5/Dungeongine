package dungeongine.api.map;

public interface Tile {
	void setPassable(boolean passable);
	boolean isPassable();

	Location getLocation();

	World getWorld();
}
