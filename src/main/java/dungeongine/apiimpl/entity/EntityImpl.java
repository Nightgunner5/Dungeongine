package dungeongine.apiimpl.entity;

import com.google.common.collect.Maps;
import dungeongine.api.entity.Entity;
import dungeongine.api.map.Location;
import dungeongine.apiimpl.StorageImpl;

import java.util.Map;

public class EntityImpl extends StorageImpl implements Entity {
	private final String id;
	private Location location;

	public EntityImpl(String id) {
		super("entity", id);
		this.id = id;
	}

	@Override
	protected void load(Map<String, Object> data) {
		location = (Location) data.get("location");
	}

	@Override
	protected Map<String, Object> getDefault() {
		return Maps.newLinkedHashMap();
	}

	@Override
	protected void serialize(Map<String, Object> data) {
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
}
