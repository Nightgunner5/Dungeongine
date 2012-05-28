package dungeongine.gui;

import com.google.common.collect.Iterators;
import dungeongine.Main;
import dungeongine.net.Connection;
import dungeongine.net.NetworkUtils;
import dungeongine.server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

public class ServerGUI extends JPanel {
	private static ServerGUI instance;
	private static JList<String> playerList;

	public ServerGUI() {
		instance = this;
		playerList = new JList<>(new AbstractListModel<String>() {
			@Override
			public int getSize() {
				return Server.clientMap.size();
			}

			@Override
			public String getElementAt(int index) {
				Set<Map.Entry<String, Connection>> entries = Server.clientMap.entrySet();
				return NetworkUtils.toString(Iterators.get(entries.iterator(), index).getValue());
			}
		});
		add(playerList);
		Main.serverStartup();
		Timer timer = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playerList.updateUI();
			}
		});
		timer.setRepeats(true);
		timer.start();
	}
}
