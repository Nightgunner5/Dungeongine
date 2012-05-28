package dungeongine.gui;

import javax.swing.*;
import java.awt.*;

public final class GUI {
	static boolean finishedSetup = false;

	private GUI() {}
	private static JFrame window;
	public static void start() {
		window = new JFrame("Dungeongine");
		window.setLayout(new BorderLayout());
		window.add(new ConsoleGUI(), BorderLayout.SOUTH);
		window.add(new ServerGUI(), BorderLayout.WEST);
		window.add(new ClientGUI(), BorderLayout.CENTER);
		window.pack();
		window.setLocationByPlatform(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		finishedSetup = true;
	}
}
