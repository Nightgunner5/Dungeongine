package dungeongine.engine

import java.util.logging.Level
import java.util.logging.Logger

@Singleton
class Ticker {
	private final LinkedList<List<Closure>> ticks = new LinkedList<>()

	void nextTick(Closure callback) {
		synchronized (ticks) {
			if (!(0 in ticks))
				ticks[0] = []
			ticks[0][] = callback
		}
	}

	void after(int delay, Closure callback) throws IllegalArgumentException {
		if (delay <= 0)
			throw new IllegalArgumentException("Delay must be positive ($delay)")
		synchronized (ticks) {
			while (!(delay in ticks))
				ticks[] = []
			ticks[delay][] = callback
		}
	}

	static {
		new Thread({
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(50)
				} catch (InterruptedException) {
					return
				}

				List<Closure> thisTick

				try {
					synchronized (ticks) {
						thisTick = Ticker.instance.ticks.removeFirst()
					}
				} catch (NoSuchElementException) {
					continue
				}

				thisTick.each { tick ->
					try { tick() } catch (Exception ex) {
						Logger.getLogger(Ticker.class.getName()).log Level.SEVERE, "Exception in tick '$tick'", ex
					}
				}
			}
		}, "Ticker").start()
	}
}
