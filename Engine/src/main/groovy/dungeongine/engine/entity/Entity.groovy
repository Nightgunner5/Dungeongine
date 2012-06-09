package dungeongine.engine.entity

import dungeongine.engine.Location
import dungeongine.engine.render.Renderable
import dungeongine.engine.render.Sprite
import dungeongine.storage.Storage

final class Entity implements Renderable {
	String uuid
	Location location
	Sprite sprite
	EntityData data

	@Override
	protected void finalize() {
		save()
	}

	void save() {
		Storage.instance.save(uuid, this)
	}

	static Entity load(String uuid) {
		Storage.instance.load(Entity.class, uuid)
	}

	static Entity create() {
		new Entity(uuid: UUID.randomUUID())
	}
}
