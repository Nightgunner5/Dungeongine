package dungeongine.gui;

import dungeongine.api.Dungeongine;
import dungeongine.api.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class ServerGUI extends JPanel {
	static final ServerGUI instance = new ServerGUI();

	private final DefaultListModel<String> players;

	private ServerGUI() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridwidth = GridBagConstraints.RELATIVE;
		add(init(new JLabel("Players")), c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(init(new JLabel("Tick")), c);
		c.gridwidth = GridBagConstraints.RELATIVE;
		add(new JList<>(players = new DefaultListModel<>()));
		Timer playersUpdate = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Set<String> have = new HashSet<>();
				for (Player player : Dungeongine.getServer().getOnlinePlayers()) {
					String name = String.format("%s [%s]", player.getName(), player.getId());
					have.add(name);
					if (players.contains(name))
						continue;
					else
						players.addElement(name);
				}
				for (Object player : players.toArray()) {
					if (!have.contains(player))
						players.removeElement(player);
				}
			}
		});
		playersUpdate.setRepeats(true);
		playersUpdate.start();
		setPreferredSize(new Dimension(400, 400));
	}

	private JLabel init(JLabel label) {
		label.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		return label;
	}
}
