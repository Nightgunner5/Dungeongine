package dungeongine.engine.render

import dungeongine.engine.Location
import groovy.transform.WithReadLock
import groovy.transform.WithWriteLock

import java.awt.Graphics
import javax.swing.JComponent

class Renderer extends JComponent {
	private final List<Renderable> renderables = []

	Location topLeft
	Location bottomRight

	@Override
	@WithReadLock
	void paint(Graphics g) {
		renderables.each {r ->
			if (isWithinBounds(r.location)) {
				drawSprite g, r.location, r.sprite
			}
		}
	}

	private void drawSprite(Graphics g, Location location, Sprite sprite) {
		int x = (location.x - topLeft.x) / (bottomRight.x - topLeft.x)
		int y = (location.y - topLeft.y) / (bottomRight.y - topLeft.y)
		g.drawImage sprite.load(), x, y, null
	}

	private boolean isWithinBounds(Location location) {
		return location.x >= topLeft.x &&
				location.y >= topLeft.y &&
				location.x <= bottomRight.x &&
				location.y <= bottomRight.y
	}

	@WithWriteLock
	void add(Renderable renderable) {
		renderables.add renderable
	}
}
