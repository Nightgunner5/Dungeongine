package dungeongine.net;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dungeongine.net.packet.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {
	private static final Logger logger = Logger.getLogger(Connection.class.getName());
	public static final long NETWORK_VERSION = 0x0L;
	private final DataInputStream in;
	private final DataOutputStream out;
	private final Thread thread;
	private static final BiMap<Integer, Class<? extends Packet>> packets = HashBiMap.create();

	static {
		packets.put(0x00, Packet00KeepAlive.class);
		packets.put(0x01, Packet01Handshake.class);
		packets.put(0x02, Packet02Chat.class);
		packets.put(0x03, Packet03SpellCast.class);
	}

	private final List<PacketListener> listeners = new LinkedList<>();

	public Connection(Socket socket) throws IOException {
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		thread = new Thread(new ConnectionHandler());
		thread.start();
	}

	public void addListener(PacketListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public boolean removeListener(PacketListener listener) {
		synchronized (listeners) {
			return listeners.remove(listener);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		thread.interrupt();
		in.close();
		out.close();
	}

	public synchronized void send(Packet packet) {
		try {
			out.writeByte(packets.inverse().get(packet.getClass()));
			packet.write(out);
		} catch (IOException ex) {
			logger.log(Level.WARNING, String.format("Exception sending packet %s", PacketUtils.toString(packet)), ex);
		}
	}

	private class ConnectionHandler implements Runnable {
		@Override
		public void run() {
nextPacket:
			while (!Thread.interrupted()) {
				try {
					Packet packet;
					int packetID = in.readUnsignedByte();
					if (packets.containsKey(packetID)) {
						packet = packets.get(packetID).newInstance();
						packet.read(in);
						synchronized (listeners) {
							for (PacketListener listener : listeners) {
								if (listener.accept(packet, Connection.this)) {
									continue nextPacket;
								}
							}
						}
						logger.log(Level.WARNING, String.format("Unhandled packet: %s", PacketUtils.toString(packet)));
					} else {
						logger.log(Level.SEVERE, String.format("Unknown packet type %s", Integer.toHexString(packetID)));
					}
				} catch (IOException | InstantiationException | IllegalAccessException ex) {
					logger.log(Level.WARNING, "Exception in packet read:", ex);
				}
			}
		}
	}
}
