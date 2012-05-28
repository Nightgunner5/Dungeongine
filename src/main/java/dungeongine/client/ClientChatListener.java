package dungeongine.client;

import dungeongine.net.Connection;
import dungeongine.net.packet.Packet;
import dungeongine.net.packet.Packet02Chat;
import dungeongine.net.packet.PacketListener;

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
