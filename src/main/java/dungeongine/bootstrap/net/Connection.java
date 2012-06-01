package dungeongine.bootstrap.net;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import dungeongine.api.Dungeongine;
import dungeongine.api.Events;
import dungeongine.api.event.player.PlayerLeaveEvent;
import dungeongine.apiimpl.event.player.PlayerLeaveEventImpl;
import dungeongine.bootstrap.net.packet.*;
import dungeongine.bootstrap.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
		packets.put(0x05, Packet05Disconnect.class);
		packets.put(0x10, Packet10MapData.class);
	}

	private final List<PacketListener> listeners = new LinkedList<>();
	public final Socket socket;

	public Connection(Socket socket) throws IOException {
		this.socket = socket;
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
			if (packet instanceof Packet01Handshake) {
				setVar("name", ((Packet01Handshake) packet).getPlayerName());
				setVar("uuid", ((Packet01Handshake) packet).getUuid());
			}
			out.writeByte(packets.inverse().get(packet.getClass()));
			packet.write(out);
		} catch (IOException ex) {
			logger.log(Level.WARNING, String.format("Exception sending packet %s", NetworkUtils.toString(packet)), ex);
		}
	}

	private final Map<String, Object> vars = Maps.newHashMap();

	public <T> T setVar(String key, T value) {
		return (T) vars.put(key, value);
	}

	public <T> T getVar(String key) {
		return (T) vars.get(key);
	}

	public void disconnect() {
		disconnect(false);
	}

	private void disconnect(boolean alreadyDead) {
		Server.dropClient(this);
		if (getVar("uuid") == null) {
			try {
				socket.close();
			} catch (IOException ex) {
			}
			return;
		}
		if (alreadyDead) {
			PlayerLeaveEvent event = new PlayerLeaveEventImpl(this);
			Events.dispatch(event);
			if (!Strings.isNullOrEmpty(event.getMessage()))
				Dungeongine.getServer().broadcastChatMessage(event.getMessage());
		} else {
			send(new Packet05Disconnect((String) getVar("uuid")));
		}
		try {
			out.flush();
			socket.close();
		} catch (IOException ex) {
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
						logger.log(Level.WARNING, String.format("Unhandled packet: %s", NetworkUtils.toString(packet)));
					} else {
						logger.log(Level.SEVERE, String.format("Unknown packet type %s", Integer.toHexString(packetID)));
					}
				} catch (IOException | InstantiationException | IllegalAccessException ex) {
					logger.log(Level.WARNING, "Exception in packet read:", ex);
					if (ex instanceof SocketException)
						Thread.currentThread().interrupt();
				}
			}
			disconnect(true);
		}
	}
}