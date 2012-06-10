package dungeongine.engine.render

import groovy.transform.Synchronized

import java.awt.image.BufferedImage
import java.lang.ref.SoftReference
import javax.imageio.ImageIO

class Sprite {
	private SoftReference<BufferedImage> imageCache
	String filename
	int left
	int top
	int width
	int height

	@Synchronized
	BufferedImage load() {
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
