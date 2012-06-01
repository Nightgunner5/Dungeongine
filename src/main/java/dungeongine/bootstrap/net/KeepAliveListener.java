package dungeongine.bootstrap.net;

import dungeongine.bootstrap.net.packet.Packet;
import dungeongine.bootstrap.net.packet.Packet00KeepAlive;
import dungeongine.bootstrap.net.packet.PacketListener;

public class KeepAliveListener implements PacketListener {
	@Override
	public boolean accept(Packet packet, Connection connection) {
		return packet instanceof Packet00KeepAlive;
	}
}
