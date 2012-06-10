package dungeongine.engine.map

import dungeongine.engine.Location
import dungeongine.engine.render.Renderable
import dungeongine.engine.render.Sprite
import dungeongine.storage.Storage

/**
 * Represents a tile on the map.
 */
final class Tile implements Renderable {
	/**
	 * The location of this tile
	 */
	Location location
	/**
	 * The sprite this tile displays.
	 */
	Sprite sprite
	/**
	 * Whether or not the tile can be crossed by players.
	 */
	boolean passable

	/**
	 * Get a tile from the persistent storage. If no tile is found for the given location, a new one is created.
	 */
	static Tile get(Location location) {
		Tile tile = Storage.instance.load(Tile.class, location.toString())
		tile.location = location
		tile
	}

	/**
	 * Save this Tile in the persistent storage.
	 */
	void save() {
		Storage.instance.save(location.toString(), this)
	}
}
