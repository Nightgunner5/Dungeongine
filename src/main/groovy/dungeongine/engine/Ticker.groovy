package dungeongine.engine

import groovy.transform.PackageScope

import java.util.logging.Level
import java.util.logging.Logger

final class Ticker {
	private static final LinkedList<List<Closure>> ticks = new LinkedList<>()

	private Ticker() {}

	static void nextTick(Closure callback) {
		synchronized (ticks) {
			if (ticks.isEmpty())
				ticks.add([])
			ticks[0].add(callback)
		}
	}

	static void after(int delay, Closure callback) throws IllegalArgumentException {
		if (delay <= 0)
			throw new IllegalArgumentException("Delay must be positive ($delay)")
		synchronized (ticks) {
			while (ticks.size() <= delay)
				ticks.add([])
			ticks[delay].add(callback)
		}
	}

	private static final Object[] tickLock = new Object[0]

	static void waitForNextTick() {
		synchronized (tickLock) {
			tickLock.wait()
		}
	}

	@PackageScope
	static final Thread thread
	static {
		thread = new Thread({
			while (!Thread.interrupted()) {
				synchronized (tickLock) {
					tickLock.notifyAll()
				}
				try {
					Thread.sleep(50)
				} catch (InterruptedException) {
					return
				}

				List<Closure> thisTick

				try {
					synchronized (ticks) {
						thisTick = ticks.removeFirst()
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
		}, "Ticker")
		thread.daemon = true
		thread.start()
	}
}
