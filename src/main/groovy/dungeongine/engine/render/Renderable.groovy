package dungeongine.engine.render

import dungeongine.engine.Location

/**
 * Represents an object that can be rendered by a {@link Renderer}.
 */
public interface Renderable {
	/**
	 * Gets the location of this object within the map.
	 */
	Location getLocation()

	/**
	 * Gets the sprite that should be displayed for this
	 */
	Sprite getSprite()
}