package dungeongine.engine.entity

import dungeongine.engine.Location
import dungeongine.engine.render.Renderable
import dungeongine.engine.render.Sprite
import dungeongine.storage.Storage

/**
 * A non-static object that may or may not be interactive.
 */
final class Entity implements Renderable {
	/**
	 * The unique ID of this entity. It is a very bad idea to change this manually.
	 */
	String uuid
	/**
	 * The location of this entity.
	 */
	Location location
	/**
	 * A graphical representation of this entity.
	 */
	Sprite sprite
	/**
	 * Data specific to this entity.
	 */
	EntityData data

	/**
	 * Saves this entity in persistent storage
	 */
	void save() {
		Storage.instance.save(uuid, this)
	}

	/**
	 * Loads an entity from persistent storage. If no entity is found with the given uuid, a new one is created.
	 */
	static Entity load(String uuid) {
		Entity entity = Storage.instance.load(Entity.class, uuid)
		entity.uuid = uuid
		entity
	}

	/**
	 * Creates and returns an entity with a random uuid.
	 */
	static Entity create() {
		new Entity(uuid: UUID.randomUUID())
	}
}
