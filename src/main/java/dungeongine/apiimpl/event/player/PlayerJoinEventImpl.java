package dungeongine.apiimpl.event.player;

import dungeongine.api.entity.Player;
import dungeongine.api.event.player.PlayerJoinEvent;
import dungeongine.apiimpl.entity.PlayerImpl;
import dungeongine.net.Connection;

import java.net.InetAddress;

public class PlayerJoinEventImpl implements PlayerJoinEvent {
	private final Connection connection;
	private String message;
	private boolean cancelled;

	public PlayerJoinEventImpl(Connection connection) {
		this.connection = connection;
		this.message = String.format("%s connected.", connection.getVar("name"));
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public InetAddress getAddress() {
		return connection.socket.getInetAddress();
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public Player getPlayer() {
		return PlayerImpl.get(connection);
	}
}
