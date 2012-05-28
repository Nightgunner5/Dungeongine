package dungeongine.net.packet;

import dungeongine.net.Connection;

public interface PacketListener {
	/**
	 * Process a packet.
	 *
	 * @param packet The recieved packet
	 * @param connection The connection that the packet was sent on
	 * @return True if the packet was consumed by this listener, false otherwise.
	 */
	boolean accept(Packet packet, Connection connection);
}
