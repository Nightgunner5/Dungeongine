package dungeongine.gui;

import dungeongine.bootstrap.net.NetworkUtils;
import dungeongine.bootstrap.server.Server;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;

public final class GUI {
	static boolean finishedSetup = false;

	private GUI() {
	}

	private static JFrame window;
	private static JFrame server;

	public static void start() {
		try {
			UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException ex1) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ex2) {
				// Oh well, it's just appearances anyway.
			}
		}
		window = new JFrame("Dungeongine");
		window.setLayout(new BorderLayout());
		window.add(ConsoleGUI.instance, BorderLayout.SOUTH);
		window.add(StartupGUI.instance, BorderLayout.CENTER);
		window.pack();
		window.setLocationByPlatform(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		finishedSetup = true;
	}

	public static void serverStartup() {
		Server.start();
		NetworkUtils.registerServerListeners();

		server = new JFrame("Dungeongine Host");
		server.add(ServerGUI.instance);
		server.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		server.pack();
		server.setLocationByPlatform(true);
		server.setVisible(true);
	}

	public static void clientStartup(InetAddress host, String name) {
		window.remove(StartupGUI.instance);
		window.add(ClientGUI.instance, BorderLayout.CENTER);
		if (ClientGUI.connect(host)) {
			ClientGUI.handshake(name);
			window.pack();
		} else {
			window.remove(ClientGUI.instance);
			window.add(StartupGUI.instance, BorderLayout.CENTER);
			StartupGUI.reenable();
		}
	}
}
