package dungeongine.server;

import dungeongine.api.Events;
import dungeongine.api.event.player.PlayerJoinEvent;
import dungeongine.apiimpl.event.player.PlayerJoinEventImpl;
import dungeongine.net.Connection;
import dungeongine.net.packet.Packet;
import dungeongine.net.packet.Packet01Handshake;
import dungeongine.net.packet.Packet02Chat;
import dungeongine.net.packet.PacketListener;

import java.util.UUID;

public class ServerHandshakeListener implements PacketListener {
	@Override
	public boolean accept(Packet packet, final Connection connection) {
		if (packet instanceof Packet01Handshake) {
			final Packet01Handshake handshake = (Packet01Handshake) packet;
			try {
				if (!UUID.fromString(handshake.getUuid()).toString().equals(handshake.getUuid())) {
					connection.disconnect();
					return true;
				}
			} catch (IllegalArgumentException ex) {
				connection.disconnect();
				return true;
			}
			handshake.setPlayerName(handshake.getPlayerName().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
			if (handshake.getVersion() == Connection.NETWORK_VERSION && !Server.clientMap.containsKey(handshake.getUuid()) && !handshake.getPlayerName().trim().isEmpty()) {
				Server.clientMap.put(handshake.getUuid(), connection);
				connection.setVar("uuid", handshake.getUuid());
				connection.setVar("name", handshake.getPlayerName().trim());
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
