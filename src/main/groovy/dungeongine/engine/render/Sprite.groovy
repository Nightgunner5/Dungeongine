package dungeongine.engine.render

import groovy.transform.Synchronized

import java.awt.image.BufferedImage
import java.lang.ref.SoftReference
import javax.imageio.ImageIO

/**
 * Represents a sprite for render in game. It is suggested that sprites be 32x32 pixels. Multiple sprites may be
 * included on a single spritesheet.
 */
class Sprite {
	private transient SoftReference<BufferedImage> imageCache
	/**
	 * The name of the file this sprite's image is in.
	 */
	String filename
	/**
	 * The x coordinate of the left edge of this sprite on the spritesheet.
	 */
	int left
	/**
	 * The y coordinate of the top edge of this sprite on the spritesheet.
	 */
	int top
	/**
	 * The width of this sprite. Defaults to 32.
	 */
	int width = 32
	/**
	 * The height of this sprite. Defaults to 32.
	 */
	int height = 32

	/**
	 * Gets the image this sprite represents. This will load the image if it has not already been loaded.
	 */
	@Synchronized
	BufferedImage getImage() {
		BufferedImage image = imageCache.get()
		if (image == null) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
			imageCache = new SoftReference<>(image)
			BufferedImage sheet = ImageIO.read(Sprite.classLoader.getResourceAsStream(filename))
			image.graphics.drawImage sheet, -left, -top, null
		}
		return image
	}
}
