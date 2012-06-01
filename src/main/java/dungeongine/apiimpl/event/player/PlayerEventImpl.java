package dungeongine.apiimpl.event.player;

import dungeongine.api.entity.Player;
import dungeongine.api.event.Cancellable;
import dungeongine.api.event.player.PlayerEvent;
import dungeongine.apiimpl.entity.PlayerImpl;
import dungeongine.bootstrap.net.Connection;

public class PlayerEventImpl implements PlayerEvent, Cancellable {
	private final Connection connection;
	private boolean cancelled;

	public PlayerEventImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Player getPlayer() {
		return PlayerImpl.get(connection);
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
}
