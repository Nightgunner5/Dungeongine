package dungeongine.apiimpl.map;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dungeongine.api.entity.Entity;
import dungeongine.api.entity.Player;
import dungeongine.api.map.Location;
import dungeongine.api.map.Tile;
import dungeongine.api.map.World;

public class WorldImpl implements World {
	private final String name;

	public WorldImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	private static final LoadingCache<Location, Tile> tileCache = CacheBuilder.newBuilder().softValues().build(new CacheLoader<Location, Tile>() {
		@Override
		public Tile load(Location key) throws Exception {
			return new TileImpl(key);
		}
	});

	@Override
	public Tile getTileAt(Location location) throws IllegalArgumentException {
		Preconditions.checkArgument(location.getWorldName().equals(name));
		return tileCache.getUnchecked(location);
	}

	@Override
	public Tile getTileAt(long x, long y) {
		return getTileAt(new Location(this, x, y));
	}

	@Override
	public Entity[] getEntities() {
		return new Entity[0];
	}

	@Override
	public Player[] getPlayers() {
		return new Player[0];
	}
}
