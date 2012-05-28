package dungeongine.net;

import dungeongine.net.packet.Packet;
import dungeongine.net.packet.Packet00KeepAlive;
import dungeongine.net.packet.PacketListener;

public class KeepAliveListener implements PacketListener {
	@Override
	public boolean accept(Packet packet, Connection connection) {
		return packet instanceof Packet00KeepAlive;
	}
}
