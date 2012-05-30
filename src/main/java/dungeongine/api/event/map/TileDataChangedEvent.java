package dungeongine.api.event.map;

import dungeongine.api.event.DataChangedEvent;
import dungeongine.api.map.Tile;

/** Fired when a property of a {@link dungeongine.api.map.Tile} is changed. */
public interface TileDataChangedEvent<T> extends DataChangedEvent<T> {
	/** Gets the tile on which a property was changed. */
	Tile getTile();
}
