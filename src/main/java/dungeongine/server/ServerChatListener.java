package dungeongine.server;

import dungeongine.net.Connection;
import dungeongine.net.packet.Packet;
import dungeongine.net.packet.PacketListener;

public class ServerChatListener implements PacketListener {
	@Override
	public boolean accept(Packet packet, Connection connection) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
