package dungeongine.engine

import java.util.concurrent.atomic.AtomicBoolean

class TickerTest extends GroovyTestCase {
	void testNextTick() {
		final AtomicBoolean value = new AtomicBoolean(false)
		assert !value.get()
		Ticker.nextTick {value.set true}
		Ticker.waitForNextTick()
		assert value.get()
	}

	void testSchedule() {
		final AtomicBoolean value = new AtomicBoolean(false)
		Ticker.after 2, {value.set true}
		Ticker.waitForNextTick()
		assert !value.get()
		Ticker.waitForNextTick()
		Ticker.waitForNextTick()
		assert value.get()
	}
}
