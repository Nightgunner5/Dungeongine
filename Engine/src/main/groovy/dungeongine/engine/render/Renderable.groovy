package dungeongine.engine.render

import dungeongine.engine.Location

public interface Renderable {
	Location getLocation()
	Sprite getSprite()
}