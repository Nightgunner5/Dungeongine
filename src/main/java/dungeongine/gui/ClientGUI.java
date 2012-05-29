package dungeongine.gui;

import dungeongine.client.Client;
import dungeongine.net.NetworkUtils;
import dungeongine.net.packet.Packet01Handshake;
import dungeongine.net.packet.Packet02Chat;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientGUI extends JPanel {
	static final ClientGUI instance = new ClientGUI();
	private final JPanel mainView;
	private final JEditorPane chatBox;
	private final Element chatElement;
	private final JTextField chatInput;
	private ClientGUI() {
		setLayout(new BorderLayout());
		mainView = new JPanel();
		mainView.setPreferredSize(new Dimension(640, 480));
		add(mainView, BorderLayout.NORTH);
		chatBox = new JEditorPane();
		chatBox.setContentType("text/html");
		chatBox.setText("<html><head></head><body><p style='font-family: sans-serif;'></p></body></html>");
		chatElement = chatBox.getDocument().getDefaultRootElement().getElement(1).getElement(0);
		chatBox.setAutoscrolls(true);
		chatBox.setEditable(false);
		chatBox.setPreferredSize(new Dimension(200, 100));
		add(new JScrollPane(chatBox));
		add(chatInput = listen(new JTextField(), new Runnable() {
			@Override
			public void run() {
				Client.send(new Packet02Chat(chatInput.getText()));
				chatInput.setText("");
			}
		}), BorderLayout.SOUTH);
		chatInput.setEditable(false);
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

	public static void displayChat(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (!GUI.finishedSetup)
					return;
				HTMLDocument document = (HTMLDocument) instance.chatBox.getDocument();
				boolean atEnd = instance.chatBox.getCaretPosition() == document.getLength() - 1;
				try {
					document.insertBeforeEnd(instance.chatElement, "<br>" + message);
				} catch (IOException | BadLocationException ex) {
					throw new RuntimeException(ex);
				}
				if (atEnd)
					instance.chatBox.setCaretPosition(document.getLength() - 1);
			}
		});
	}

	public static boolean connect(InetAddress host) {
		try {
			Client.start(host);
			NetworkUtils.registerClientListeners();
			return true;
		} catch (IOException ex) {
			Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, "Connection failed.", ex);
			return false;
		}
	}

	public static void handshake(String name) {
		Logger.getLogger(ClientGUI.class.getName()).info("Logging in as '" + name.trim() + "'...");
		Client.send(new Packet01Handshake(name));
		instance.chatInput.setEditable(true);
	}
}
