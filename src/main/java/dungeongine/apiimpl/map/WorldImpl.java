package dungeongine.apiimpl.map;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dungeongine.api.Dungeongine;
import dungeongine.api.entity.Entity;
import dungeongine.api.entity.Player;
import dungeongine.api.map.Location;
import dungeongine.api.map.Tile;
import dungeongine.api.map.World;
import dungeongine.apiimpl.StorageImpl;
import dungeongine.apiimpl.entity.EntityImpl;

import java.util.*;

public class WorldImpl extends StorageImpl implements World {
	private final String name;

	public WorldImpl(String name) {
		super("world", name);
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
		return entities.toArray(new Entity[entities.size()]);
	}

	@Override
	public Player[] getPlayers() {
		Collection<Player> players = Collections2.filter(Arrays.asList(Dungeongine.getServer().getOnlinePlayers()), new Predicate<Player>() {
			@Override
			public boolean apply(Player input) {
				return input.getLocation().getWorldName().equals(name);
			}
		});
		return players.toArray(new Player[players.size()]);
	}

	private Collection<Entity> entities;

	@Override
	protected void load(Map<String, Object> data) {
		entities = Lists.newArrayList();
		for (String ent : (List<String>) data.get("entities"))
			entities.add(new EntityImpl(ent));
	}

	@Override
	protected Map<String, Object> getDefault() {
		Map<String, Object> data = Maps.newLinkedHashMap();
		data.put("entities", Collections.emptyList());
		return data;
	}

	@Override
	protected void serialize(Map<String, Object> data) {
		List<String> ents = Lists.newArrayList();
		for (Entity entity : entities)
			ents.add(entity.getId());
		data.put("entities", ents);
	}
}
