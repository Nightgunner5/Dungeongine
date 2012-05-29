package dungeongine.apiimpl.entity;

import dungeongine.api.Events;
import dungeongine.api.entity.Entity;
import dungeongine.api.map.Location;
import dungeongine.apiimpl.StorageImpl;
import dungeongine.apiimpl.event.entity.EntityDataChangedEventImpl;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class EntityImpl extends StorageImpl implements Entity {
	private final String id;
	private Location location;

	public EntityImpl(String id) {
		super("ent-" + id);
		this.id = id;
	}

	@Override
	protected void load(Map<String, Serializable> data) {
		location = (Location) data.get("location");
	}

	@Override
	protected Map<String, Serializable> getDefault() {
		return Collections.emptyMap();
	}

	@Override
	protected void serialize(Map<String, Serializable> data) {
		data.put("location", location);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void teleport(Location newLocation) {
		dataChanged("location", location, newLocation);
		location = newLocation;
	}

	protected <T> void dataChanged(String name, T oldValue, T newValue) {
		Events.dispatch(new EntityDataChangedEventImpl<>(this, name, oldValue, newValue));
	}
}
