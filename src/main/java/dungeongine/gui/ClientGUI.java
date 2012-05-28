package dungeongine.gui;

import dungeongine.Main;
import dungeongine.client.Client;
import dungeongine.net.packet.Packet02Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JPanel {
	private static ClientGUI instance;
	private static JPanel mainView;
	private static JPanel chatBox;
	private static JTextField chatInput;
	public ClientGUI() {
		instance = this;
		setLayout(new BorderLayout());
		mainView = new JPanel();
		mainView.setPreferredSize(new Dimension(640, 480));
		mainView.add(listen(new JButton("Join Game"), new Runnable() {
			@Override
			public void run() {
				Main.clientStartup(JOptionPane.showInputDialog(ClientGUI.this, "What should your character be named?"));
			}
		}));
		add(mainView, BorderLayout.NORTH);
		chatBox = new JPanel();
		chatBox.setLayout(new GridLayout(0, 1));
		chatBox.setPreferredSize(new Dimension(200, 100));
		add(new JScrollPane(chatBox, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		add(chatInput = listen(new JTextField(), new Runnable() {
			@Override
			public void run() {
				Client.send(new Packet02Chat(chatInput.getText()));
			}
		}), BorderLayout.SOUTH);
	}

	private static JButton listen(JButton button, final Runnable listener) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.run();
			}
		});
		return button;
	}

	private static JTextField listen(JTextField field, final Runnable listener) {
		field.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.run();
			}
		});
		return field;
	}

	public static void displayChat(String message) {
		chatBox.add(new JLabel(message, JLabel.LEFT));
	}
}
