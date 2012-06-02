package dungeongine.gui.components;

import dungeongine.api.item.Item;
import dungeongine.api.item.Items;
import dungeongine.apiimpl.item.ItemReference;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemGUI extends JComponent {
	private final ItemReference item;
	private final ItemTooltip tooltip;

	public ItemGUI(long id) {
		this(Items.get(id));
	}

	public ItemGUI(Item item) {
		this.item = new ItemReference(item);
		setPreferredSize(new Dimension(32, 32));
		tooltip = new ItemTooltip();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				tooltip.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				tooltip.setVisible(false);
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				tooltip.setLocation(e.getXOnScreen() + 2, e.getYOnScreen() + 2);
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO
	}

	private class ItemTooltip extends JFrame {
		public ItemTooltip() {
			setUndecorated(true);
			setType(Type.UTILITY);
			setLayout(new GridLayout(0, 1));
			JLabel name = new JLabel(item.getItem().getName());
			name.setFont(name.getFont().deriveFont(Font.BOLD));
			add(name);
			JLabel description = new JLabel(String.format("Level %d (%s)",
					item.getItem().getLevel(), item.getItem().getQuality().getName()));
			description.setFont(description.getFont().deriveFont(Font.PLAIN));
			add(description);
			pack();
		}
	}
}
