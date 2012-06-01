package dungeongine.bootstrap.client;

import dungeongine.bootstrap.net.Connection;
import dungeongine.bootstrap.net.packet.Packet;
import dungeongine.bootstrap.net.packet.Packet02Chat;
import dungeongine.bootstrap.net.packet.PacketListener;

public class ClientChatListener implements PacketListener {
	@Override
	public boolean accept(Packet packet, Connection connection) {
		if (packet instanceof Packet02Chat) {
			Client.displayChat(((Packet02Chat) packet).getMessage());
			return true;
		}
		return false;
	}
}
