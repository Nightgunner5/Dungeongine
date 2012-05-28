package dungeongine.gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ConsoleGUI extends JTextPane {
	public ConsoleGUI() {
		Logger.getLogger("").addHandler(new Handler() {
			{
				setFormatter(new SimpleFormatter());
			}

			@Override
			public void publish(LogRecord record) {
				updateTextPane(String.format("%TT [%s] %s\n", record.getMillis(), record.getLevel(), getFormatter().formatMessage(record)));
			}

			@Override
			public void flush() {
			}

			@Override
			public void close() throws SecurityException {
			}
		});
	}

	private void updateTextPane(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (!GUI.finishedSetup)
					return;
				Document document = ConsoleGUI.this.getDocument();
				try {
					document.insertString(document.getLength(), text, null);
				} catch (BadLocationException ex) {
					throw new RuntimeException(ex);
				}
				ConsoleGUI.this.setCaretPosition(document.getLength() - 1);
			}
		});
	}
}
