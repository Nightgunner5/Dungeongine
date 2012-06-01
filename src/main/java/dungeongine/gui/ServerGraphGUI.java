package dungeongine.gui;

import dungeongine.api.Dungeongine;

import javax.swing.*;
import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.Map;

public class ServerGraphGUI extends JComponent {
	public ServerGraphGUI() {
		setPreferredSize(new Dimension(300, 200));
		setMaximumSize(new Dimension(300, 200));
		setMinimumSize(new Dimension(300, 200));
		setSize(new Dimension(300, 200));
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 300, 200);
		try {
			int x = 0;
			for (Long tick : Dungeongine.Statistics.getTicks()) {
				g.setColor(tick < 45 ? Color.BLACK : Color.RED);
				g.fillRect(x, 70 - tick.intValue(), 2, tick.intValue() + 1);
				x += 3;
			}
			x = 0;
			for (Map.Entry<String, Long> collection : Dungeongine.Statistics.getSaves(true).entrySet()) {
				g.setColor(new Color(collection.getKey().hashCode()));
				g.fillRect(x, 150, collection.getValue().intValue() * 5, 10);
				x += collection.getValue().intValue() * 5;
			}
			x = 0;
			for (Map.Entry<String, Long> collection : Dungeongine.Statistics.getSaves(false).entrySet()) {
				g.setColor(new Color(collection.getKey().hashCode()));
				g.fillRect(x, 160, collection.getValue().intValue(), 10);
				x += collection.getValue().intValue();
			}
			x = 0;
			for (Map.Entry<String, Long> collection : Dungeongine.Statistics.getLoads(true).entrySet()) {
				g.setColor(new Color(collection.getKey().hashCode()));
				g.fillRect(x, 175, collection.getValue().intValue() * 5, 10);
				x += collection.getValue().intValue() * 5;
			}
			x = 0;
			for (Map.Entry<String, Long> collection : Dungeongine.Statistics.getLoads(false).entrySet()) {
				g.setColor(new Color(collection.getKey().hashCode()));
				g.fillRect(x, 185, collection.getValue().intValue(), 10);
				x += collection.getValue().intValue();
			}
		} catch (ConcurrentModificationException | NullPointerException ex) {
			// Instead of synchronizing, it's probably better to just drop the frame.
		}
		repaint();
	}
}
