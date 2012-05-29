package dungeongine.gui;

import dungeongine.Main;
import dungeongine.client.Client;
import dungeongine.net.packet.Packet02Chat;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientGUI extends JPanel {
	private static ClientGUI instance;
	private static JPanel mainView;
	private static JEditorPane chatBox;
	private static Element chatElement;
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
				mainView.removeAll();
				chatInput.setEditable(true);
			}
		}));
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

	public static void displayChat(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (!GUI.finishedSetup)
					return;
				HTMLDocument document = (HTMLDocument) chatBox.getDocument();
				boolean atEnd = chatBox.getCaretPosition() == document.getLength() - 1;
				try {
					document.insertBeforeEnd(chatElement, "<br>" + message);
				} catch (IOException | BadLocationException ex) {
					throw new RuntimeException(ex);
				}
				if (atEnd)
					chatBox.setCaretPosition(document.getLength() - 1);
			}
		});
	}
}
