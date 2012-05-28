package dungeongine.server;

import dungeongine.net.Connection;
import dungeongine.net.packet.Packet;
import dungeongine.net.packet.Packet01Handshake;
import dungeongine.net.packet.PacketListener;

public final class ServerPacketListener implements PacketListener {
	private final PacketListener listener;

	public ServerPacketListener(PacketListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean accept(Packet packet, Connection connection) {
		if (!(packet instanceof Packet01Handshake) && connection.getVar("name") == null)
			connection.disconnect();
		return listener.accept(packet, connection);
	}
}
