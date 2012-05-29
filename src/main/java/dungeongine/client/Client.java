package dungeongine.client;

import com.google.common.net.InetAddresses;
import dungeongine.Main;
import dungeongine.gui.ClientGUI;
import dungeongine.net.Connection;
import dungeongine.net.packet.Packet;
import dungeongine.net.packet.PacketListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public final class Client {
	private static final Logger logger = Logger.getLogger(Client.class.getName());

	private Client() {
	}

	private static Connection connection;

	public static void start(InetAddress address) throws IOException {
		connection = new Connection(new Socket(address, Main.PORT));
		logger.info(String.format("Client connected to %s on port %d", InetAddresses.toAddrString(address), Main.PORT));
	}

	public static void addListener(PacketListener listener) {
		connection.addListener(listener);
	}

	public static boolean removeListener(PacketListener listener) {
		return connection.removeListener(listener);
	}

	public static void send(Packet packet) {
		connection.send(packet);
	}

	public static void displayChat(String message) {
		ClientGUI.displayChat(message);
	}
}
