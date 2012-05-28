package dungeongine;

import dungeongine.client.Client;
import dungeongine.gui.GUI;
import dungeongine.net.NetworkUtils;
import dungeongine.net.packet.Packet01Handshake;
import dungeongine.server.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Main {
	public static final int PORT = 0xD61F;

	private Main() {}

	public static void main(String[] args) {
		GUI.start();
	}

	public static void clientStartup(String characterName) {
		try {
			Client.start(InetAddress.getByName("127.0.0.1"));
		} catch (UnknownHostException ex) {
			throw new RuntimeException(ex);
		}
		NetworkUtils.registerClientListeners();
		Client.send(new Packet01Handshake(characterName));
	}

	public static void serverStartup() {
		Server.start();
		NetworkUtils.registerServerListeners();
	}
}
