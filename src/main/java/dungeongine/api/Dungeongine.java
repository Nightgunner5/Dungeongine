package dungeongine.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dungeongine.api.map.World;
import dungeongine.apiimpl.ServerImpl;
import dungeongine.apiimpl.StorageImpl;

import java.util.*;
import java.util.logging.Logger;

public final class Dungeongine {
	private Dungeongine() {
	}

	private static final Server server = new ServerImpl();

	/** Gets the server instance. */
	public static Server getServer() {
		return server;
	}

	/**
	 * Shortcut for {@link dungeongine.api.Dungeongine#getServer()}.{@link dungeongine.api.Server#getWorld(String)
	 * getWorld(String)}.
	 */
	public static World getWorld(String name) {
		return server.getWorld(name);
	}

	/** Shortcut for {@link dungeongine.api.Dungeongine.Ticker#getTick()}. */
	public static long getTick() {
		return Ticker.getTick();
	}

	static {
		getTick(); // Start ticker
	}

	public static final class Statistics {
		private static final int MAX_LENGTH = 100;

		private Statistics() {
		}

		static {
			StorageImpl.register(new StorageImpl.Statistics() {
				@Override
				public void recordSave(String collection) {
					Dungeongine.Statistics.recordSave(collection);
				}

				@Override
				public void recordLoad(String collection) {
					Dungeongine.Statistics.recordLoad(collection);
				}
			});
		}

		private static final LinkedList<Long> ticks = Lists.newLinkedList();
		private static final List<Long> ticksRead = Collections.unmodifiableList(ticks);

		private static synchronized void recordTick(long tick) {
			if (ticks.size() > MAX_LENGTH)
				ticks.removeFirst();
			ticks.addLast(tick);
			if (Ticker.getTick() % 20 == 0) {
				lastLoads.clear();
				lastSaves.clear();
			}
		}

		public static long getLastTick() {
			return ticks.isEmpty() ? 0 : ticks.getLast();
		}

		public static List<Long> getTicks() {
			return ticksRead;
		}

		private static final Map<String, Long> totalSaves = Maps.newHashMap();
		private static final Map<String, Long> totalSavesRead = Collections.unmodifiableMap(totalSaves);
		private static final Map<String, Long> lastSaves = Maps.newHashMap();
		private static final Map<String, Long> lastSavesRead = Collections.unmodifiableMap(lastSaves);

		private static synchronized void recordSave(String collection) {
			totalSaves.put(collection, totalSaves.containsKey(collection) ? 1 + totalSaves.get(collection) : 1);
			lastSaves.put(collection, lastSaves.containsKey(collection) ? 1 + lastSaves.get(collection) : 1);
		}

		public static Map<String, Long> getSaves(boolean onlyRecent) {
			if (onlyRecent)
				return lastSavesRead;
			return totalSavesRead;
		}

		private static final Map<String, Long> totalLoads = Maps.newHashMap();
		private static final Map<String, Long> totalLoadsRead = Collections.unmodifiableMap(totalLoads);
		private static final Map<String, Long> lastLoads = Maps.newHashMap();
		private static final Map<String, Long> lastLoadsRead = Collections.unmodifiableMap(lastLoads);

		private static synchronized void recordLoad(String collection) {
			totalLoads.put(collection, totalLoads.containsKey(collection) ? 1 + totalLoads.get(collection) : 1);
			lastLoads.put(collection, lastLoads.containsKey(collection) ? 1 + lastLoads.get(collection) : 1);
		}

		public static Map<String, Long> getLoads(boolean onlyRecent) {
			if (onlyRecent)
				return lastLoadsRead;
			return totalLoadsRead;
		}
	}

	public static final class Ticker {
		private static long tick = 0;
		/** A tick is 50 milliseconds, making 20 ticks per second. */
		public static final long TICK_LENGTH_MS = 50L;

		private Ticker() {
		}

		static {
			new Thread("Tick thread") {
				@Override
				public void run() {
					while (!Thread.interrupted()) {
						long tickStart = System.currentTimeMillis();
						tick++;
						doTick();
						long tickLength = System.currentTimeMillis() - tickStart;
						Statistics.recordTick(tickLength);
						if (tickLength >= TICK_LENGTH_MS) {
							Logger.getLogger(Ticker.class.getName()).warning(String.format("Tick took %dms (%dms is normal)", tickLength, TICK_LENGTH_MS));
						} else {
							try {
								Thread.sleep(TICK_LENGTH_MS - tickLength);
							} catch (InterruptedException ex) {
								return;
							}
						}
					}
				}
			}.start();
		}

		private static final LinkedList<List<Runnable>> scheduledTicks = Lists.newLinkedList();

		private static void doTick() {
			List<Runnable> ticks = Collections.emptyList();
			synchronized (scheduledTicks) {
				if (!scheduledTicks.isEmpty()) {
					ticks = scheduledTicks.removeFirst();
				}
			}
			for (Runnable scheduled : ticks) {
				long start = System.currentTimeMillis();
				scheduled.run();
				long time = System.currentTimeMillis() - start;
				if (time > 5) {
					Logger.getLogger(Ticker.class.getName()).warning(String.format("%s tick took %dms!", scheduled, time));
				}
			}
		}

		/** Schedules a {@link Runnable} to be {@link Runnable#run() run} on the next tick. */
		public static void scheduleTick(Runnable scheduled) {
			scheduleTick(scheduled, 1);
		}

		/**
		 * Schedules a {@link Runnable} to be {@link Runnable#run() run} in a given amount of ticks. The number of ticks must
		 * be between 1 and 5000 inclusive.
		 */
		public static void scheduleTick(Runnable scheduled, long ticks) {
			if (ticks < 1 || ticks > 5000)
				throw new IndexOutOfBoundsException();
			synchronized (scheduledTicks) {
				while (scheduledTicks.size() < ticks) {
					scheduledTicks.add(new ArrayList<Runnable>());
				}
				scheduledTicks.get((int) (ticks - 1)).add(scheduled);
			}
		}

		/** Gets the current tick number. */
		public static long getTick() {
			return tick;
		}
	}
}
