package dungeongine.engine.render

import dungeongine.engine.Location
import groovy.transform.WithReadLock
import groovy.transform.WithWriteLock

import java.awt.Graphics
import javax.swing.JComponent
import java.awt.Dimension

/**
 * A {@link JComponent component} that can render {@link Renderable}s.
 */
class Renderer extends JComponent {
	private final List<Renderable> renderables = []

	/**
	 * The top left corner of this renderer's view.
	 */
	Location topLeft
	/**
	 * The bottom right corner of this renderer's view.
	 */
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
		int x = (location.x - topLeft.x) * width / (bottomRight.x - topLeft.x)
		int y = (location.y - topLeft.y) * height / (bottomRight.y - topLeft.y)
		int w = 32 * width / (bottomRight.x - topLeft.x)
		int h = 32 * height / (bottomRight.y - topLeft.y)
		g.drawImage sprite.image, x, y, w, h, null
	}

	private boolean isWithinBounds(Location location) {
		return location.x >= topLeft.x &&
				location.y >= topLeft.y &&
				location.x <= bottomRight.x &&
				location.y <= bottomRight.y
	}

	@Override
	Dimension getPreferredSize() {
		Location dimensions = bottomRight - topLeft
		return new Dimension((int) dimensions.x * 32, (int) dimensions.y * 32)
	}

	/**
	 * Add a {@link Renderable} to the list of rendered objects. It will only be rendered if it is within the
	 * bounds set by {@link #topLeft} and {@link #bottomRight}.
	 */
	@WithWriteLock
	void add(Renderable renderable) {
		renderables.add renderable
	}

	/**
	 * Remove a {@link Renderable} from the list of rendered objects.
	 */
	@WithWriteLock
	boolean remove(Renderable renderable) {
		renderables.remove renderable
	}

	/**
	 * Determines whether a {@link Renderable} is in the list of rendered objects.
	 */
	@WithReadLock
	boolean contains(Renderable renderable) {
		renderables.contains renderable
	}
}
