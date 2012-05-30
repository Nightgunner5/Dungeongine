package dungeongine.apiimpl.map;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import dungeongine.api.Events;
import dungeongine.api.map.Location;
import dungeongine.api.map.Tile;
import dungeongine.api.map.World;
import dungeongine.apiimpl.StorageImpl;
import dungeongine.apiimpl.event.map.TileDataChangedEventImpl;

import java.io.Serializable;
import java.util.Map;

public class TileImpl extends StorageImpl implements Tile {
	private Location location;
	private boolean passable;

	public TileImpl(Location location, boolean passable) {
		super(String.format("tile-%s-%s-%s", location.getWorldName(), Long.toHexString(location.getX()), Long.toHexString(location.getY())));
		this.location = location;
		this.passable = passable;
	}

	@Override
	public void setPassable(boolean passable) {
		dataChanged("passable", this.passable, passable);
		this.passable = passable;
	}

	@Override
	public boolean isPassable() {
		return passable;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public World getWorld() {
		return location.getWorld();
	}

	@Override
	protected void load(Map<String, Serializable> data) {
		this.location = (Location) data.get("location");
		this.passable = (Boolean) data.get("passable");
	}

	@Override
	protected Map<String, Serializable> getDefault() {
		Map<String, Serializable> data = Maps.newLinkedHashMap();
		data.put("passable", Boolean.FALSE);
		return data;
	}

	@Override
	protected void serialize(Map<String, Serializable> data) {
		data.put("location", location);
		data.put("passable", passable);
	}

	@Override
	protected <T> void dataChanged(String name, T oldValue, T newValue) {
		if (!Objects.equal(oldValue, newValue))
			Events.dispatch(new TileDataChangedEventImpl<>(this, name, oldValue, newValue));
	}
}
