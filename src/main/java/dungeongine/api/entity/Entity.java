package dungeongine.api.entity;

import dungeongine.api.map.Location;
import dungeongine.api.Storage;

/**
 * Entities are things such as players, NPCs, and resource nodes. Anything that can be interacted with in any way is an
 * entity.
 */
public interface Entity extends Storage {
	/** Get the unique identifier for this entity. The identifier is safe for storage, but the Entity object itself is not. */
	String getId();

	/**
	 * Get the location of this entity.
	 *
	 * @see #teleport(dungeongine.api.map.Location)
	 */
	Location getLocation();

	/**
	 * Set the location of this entity.
	 *
	 * @see #getLocation()
	 */
	void teleport(Location newLocation);
}
