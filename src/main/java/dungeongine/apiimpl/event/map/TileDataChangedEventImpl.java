package dungeongine.apiimpl.event.map;

import dungeongine.api.event.map.TileDataChangedEvent;
import dungeongine.api.map.Tile;
import dungeongine.apiimpl.event.DataChangedEventImpl;

public class TileDataChangedEventImpl<T> extends DataChangedEventImpl<T> implements TileDataChangedEvent<T> {
	private final Tile tile;

	public TileDataChangedEventImpl(Tile tile, String fieldName, T oldValue, T newValue) {
		super(fieldName, oldValue, newValue);
		this.tile = tile;
	}

	@Override
	public Tile getTile() {
		return tile;
	}
}
