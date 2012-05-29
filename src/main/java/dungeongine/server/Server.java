package dungeongine.server;

import com.google.common.collect.Maps;
import dungeongine.Main;
import dungeongine.api.Dungeongine;
import dungeongine.net.Connection;
import dungeongine.net.packet.Packet;
import dungeongine.net.packet.PacketListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Server implements Runnable {

	private Server() {}

	private static Thread thread;
	private static final Server server = new Server();
	public static void start() {
		thread = new Thread(server, "Server thread");
		thread.start();
	}

	public static void stop() {
		synchronized (clients) {
			for (Connection client : clients)
				client.disconnect();
			clients.clear();
			clientMap.clear();
		}
		try {
			server.serverSocket.close();
			server.serverSocket = null;
		} catch (Exception ex) {
			Logger.getLogger(Server.class.getName()).log(Level.WARNING, "Could not close server socket", ex);
		}
		if (thread != null)
			thread.interrupt();
		thread = null;
	}

	private static final Collection<Connection> clients = new LinkedList<>();
	public static final Map<String, Connection> clientMap = Maps.newConcurrentMap();

	private static List<PacketListener> listeners = new LinkedList<>();
	public static void registerListener(PacketListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
			synchronized (clients) {
				for (Connection client : clients) {
					client.addListener(listener);
				}
			}
		}
	}

	public static boolean unregisterListener(PacketListener listener) {
		synchronized (listeners) {
			boolean ret = listeners.remove(listener);
			synchronized (clients) {
				for (Connection client : clients) {
					client.removeListener(listener);
				}
			}
			return ret;
		}
	}

	public static void broadcast(Packet packet) {
		synchronized (clients) {
			for (Connection client : clients) {
				client.send(packet);
			}
		}
	}

	private ServerSocket serverSocket;
	@Override
	public void run() {
		Logger logger = Logger.getLogger(Server.class.getName());
		try {
			serverSocket = new ServerSocket(Main.PORT);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Server failed to start", ex);
			return;
		}
		Logger.getLogger(Server.class.getName()).info("Server listening on port " + Main.PORT);
		Dungeongine.getServer().reloadPlugins();
		while (!Thread.interrupted()) {
			try {
				Socket socket = serverSocket.accept();
				Connection connection = new Connection(socket);
				synchronized (listeners) {
					for (PacketListener listener : listeners) {
						connection.addListener(listener);
					}
				}
				synchronized (clients) {
					clients.add(connection);
				}
			} catch (IOException ex) {
				logger.log(Level.WARNING, "Client connection failed", ex);
			}
		}
		Logger.getLogger(Server.class.getName()).info("Stopping server...");
	}

	public static void dropClient(Connection connection) {
		synchronized (clients) {
			clients.remove(connection);
			if (connection.getVar("uuid") != null)
				clientMap.remove(connection.getVar("uuid"));
		}
	}
}
