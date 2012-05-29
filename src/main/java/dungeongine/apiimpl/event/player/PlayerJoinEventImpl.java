package dungeongine.apiimpl.event.player;

import com.google.common.net.InetAddresses;
import dungeongine.api.event.player.PlayerJoinEvent;
import dungeongine.net.Connection;

import java.net.InetAddress;

public class PlayerJoinEventImpl extends PlayerEventImpl implements PlayerJoinEvent {
	private final Connection connection;
	private String message;

	public PlayerJoinEventImpl(Connection connection) {
		super(connection);
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
	public String toString() {
		return String.format("PlayerJoinEvent {player = %s, address = '%s', message = '%s'}", getPlayer(), InetAddresses.toAddrString(getAddress()), message);
	}
}
