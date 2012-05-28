package dungeongine.server;

import dungeongine.api.Events;
import dungeongine.api.event.player.PlayerJoinEvent;
import dungeongine.apiimpl.event.player.PlayerJoinEventImpl;
import dungeongine.net.Connection;
import dungeongine.net.packet.*;

public class ServerHandshakeListener implements PacketListener {
	@Override
	public boolean accept(Packet packet, final Connection connection) {
		if (packet instanceof Packet01Handshake) {
			final Packet01Handshake handshake = (Packet01Handshake) packet;
			if (handshake.getVersion() == Connection.NETWORK_VERSION && !Server.clientMap.containsKey(handshake.getUuid())) {
				Server.clientMap.put(handshake.getUuid(), connection);
				connection.setVar("uuid", handshake.getUuid());
				connection.setVar("name", handshake.getPlayerName());
				PlayerJoinEvent event = new PlayerJoinEventImpl(connection);
				Events.dispatch(event);
				if (event.isCancelled()) {
					connection.disconnect();
					return true;
				}
				if (event.getMessage() != null)
					Server.broadcast(new Packet02Chat(event.getMessage()));
			} else {
				connection.disconnect();
			}
			return true;
		}
		return false;
	}
}
