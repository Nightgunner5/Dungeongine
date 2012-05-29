package dungeongine.gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ConsoleGUI extends JTextPane {
	private static final ConsoleGUI _instance = new ConsoleGUI();
	static final JScrollPane instance = new JScrollPane(_instance, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	static {
		instance.setMinimumSize(new Dimension(-1, 100));
		instance.setMaximumSize(new Dimension(-1, 100));
		instance.setPreferredSize(new Dimension(-1, 100));
	}

	private ConsoleGUI() {
		setEditable(false);

		Logger.getLogger("").addHandler(new Handler() {
			{
				setFormatter(new SimpleFormatter());
			}

			@Override
			public void publish(LogRecord record) {
				updateTextPane(String.format("%TT [%s] %s %s%n", record.getMillis(), record.getLevel(), getFormatter().formatMessage(record), record.getThrown() == null ? "" : record.getThrown()));
			}

			@Override
			public void flush() {
			}

			@Override
			public void close() throws SecurityException {
			}
		});
	}

	private static void updateTextPane(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (!GUI.finishedSetup)
					return;
				Document document = _instance.getDocument();
				try {
					document.insertString(document.getLength(), text, null);
				} catch (BadLocationException ex) {
					throw new RuntimeException(ex);
				}
				_instance.setCaretPosition(document.getLength() - 1);
			}
		});
	}
}
