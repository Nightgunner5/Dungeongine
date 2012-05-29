package dungeongine.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class StartupGUI extends JPanel {
	static final StartupGUI instance = new StartupGUI();
	private final JTextField fieldName;
	private final JTextField fieldAddress;
	private final JButton startServer;
	private final JButton joinServer;

	private StartupGUI() {
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);
		c.anchor = GridBagConstraints.EAST;

		JLabel labelName = new JLabel("Character Name");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(labelName, c);

		fieldName = new JTextField();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		fieldName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				update(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				update(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				update(e);
			}

			private void update(DocumentEvent e) {
				setAllowStartGameA(!fieldName.getText().trim().isEmpty());
			}
		});
		add(fieldName, c);
		init(labelName, fieldName);

		JLabel labelAddress = new JLabel("Host IP");
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		add(labelAddress, c);

		fieldAddress = new JTextField("127.0.0.1");
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		fieldAddress.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				update(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				update(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				update(e);
			}

			private void update(DocumentEvent e) {
				try {
					InetAddress.getByName(fieldAddress.getText());
					setAllowStartGameB(true);
				} catch (UnknownHostException ex) {
					setAllowStartGameB(false);
				}
			}
		});
		add(fieldAddress, c);
		init(labelAddress, fieldAddress);

		startServer = new JButton("Start");
		startServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				disableAllInputs();
				GUI.serverStartup();
				try {
					GUI.clientStartup(InetAddress.getLocalHost(), fieldName.getText());
				} catch (UnknownHostException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		startServer.setEnabled(false);

		joinServer = new JButton("Join");
		joinServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				disableAllInputs();
				try {
					GUI.clientStartup(InetAddress.getByName(fieldAddress.getText()), fieldName.getText());
				} catch (UnknownHostException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		joinServer.setEnabled(false);

		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		JPanel buttonRow = new JPanel(new GridLayout());
		buttonRow.add(startServer);
		buttonRow.add(joinServer);
		add(buttonRow, c);
	}

	private void disableAllInputs() {
		fieldName.setEditable(false);
		fieldAddress.setEditable(false);
		startServer.setEnabled(false);
		joinServer.setEnabled(false);
	}

	private boolean allowA = false, allowB = true;
	private void setAllowStartGameA(boolean allow) {
		allowA = allow;
		startServer.setEnabled(allow);
		joinServer.setEnabled(allow && allowB);
	}

	private void setAllowStartGameB(boolean allow) {
		allowB = allow;
		joinServer.setEnabled(allowA && allow);
	}

	private void init(JLabel label, JTextField field) {
		label.setLabelFor(field);
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				((JLabel) e.getComponent()).getLabelFor().requestFocus();
			}
		});
		label.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		label.setPreferredSize(label.getPreferredSize());
	}

	public static void reenable() {
		instance.fieldName.setEditable(true);
		instance.fieldAddress.setEditable(true);
		instance.startServer.setEnabled(true);
		instance.joinServer.setEnabled(true);
	}
}
