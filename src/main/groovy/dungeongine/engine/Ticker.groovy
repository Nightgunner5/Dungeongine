package dungeongine.engine

import groovy.transform.PackageScope

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Allows scheduling of events based on 50ms ticks.
 */
final class Ticker {
	private static final LinkedList<List<Closure>> ticks = new LinkedList<>()

	private Ticker() {}

	/**
	 * Schedules a Closure to be called on the next tick.
	 */
	static void nextTick(Closure callback) {
		synchronized (ticks) {
			if (ticks.isEmpty())
				ticks.add([])
			ticks[0].add(callback)
		}
	}

	/**
	 * Schedules a Closure to be called after a given delay of ticks. A delay of 0 would be equivalent to
	 * the nextTick method, but non-positive delays are not allowed.
	 */
	static void after(int delay, Closure callback) throws IllegalArgumentException {
		if (delay <= 0)
			throw new IllegalArgumentException("Delay must be positive ($delay)")
		synchronized (ticks) {
			while (ticks.size() <= delay)
				ticks.add([])
			ticks[delay].add(callback)
		}
	}

	private static final Object[] tickStart = new Object[0]
	private static final Object[] tickEnd = new Object[0]

	/**
	 * Pauses the current thread until the next ticker tick completes.
	 */
	static void waitForNextTick() {
		synchronized (tickStart) {
			tickStart.wait()
		}
		synchronized (tickEnd) {
			tickEnd.wait()
		}
	}

	@PackageScope
	static final Thread thread
	static {
		thread = new Thread({
			long nextTick = System.currentTimeMillis()
			while (!Thread.interrupted()) {
				synchronized (tickEnd) {
					tickEnd.notifyAll()
				}
				nextTick += 50
				Thread.sleep(Math.max(0, nextTick - System.currentTimeMillis()))

				List<Closure> thisTick

				try {
					synchronized (ticks) {
						thisTick = ticks.removeFirst()
					}
				} catch (NoSuchElementException) {
					continue
				}

				synchronized (tickStart) {
					tickStart.notifyAll()
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
