package dungeongine.apiimpl.entity;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dungeongine.api.entity.Player;
import dungeongine.net.Connection;

public class PlayerImpl implements Player {
	private final Connection connection;

	public PlayerImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public String getId() {
		return connection.getVar("uuid");
	}

	@Override
	public String getName() {
		return connection.getVar("name");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PlayerImpl player = (PlayerImpl) o;

		return getId().equals(player.getId());

	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	private static final LoadingCache<Connection, Player> playerCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Connection, Player>() {
		@Override
		public Player load(Connection key) throws Exception {
			return new PlayerImpl(key);
		}
	}
	);
	public static Player get(Connection connection) {
		return playerCache.getUnchecked(connection);
	}
}
