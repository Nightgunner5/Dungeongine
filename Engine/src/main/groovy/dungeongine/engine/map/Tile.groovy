package dungeongine.engine.map

import dungeongine.engine.render.Renderable
import dungeongine.engine.Location
import dungeongine.engine.render.Sprite

class Tile implements Renderable {
	Location location
	Sprite sprite
	boolean passable
}
