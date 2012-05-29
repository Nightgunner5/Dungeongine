package dungeongine.api.event.map;

import dungeongine.api.event.DataChangedEvent;
import dungeongine.api.map.Tile;

public interface TileDataChangedEvent<T> extends DataChangedEvent<T> {
	Tile getTile();
}
