package dungeongine.api;

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

	public static Server getServer() {
		return server;
	}

	public static World getWorld(String name) {
		return server.getWorld(name);
	}

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
					Statistics.recordSave(collection);
				}

				@Override
				public void recordLoad(String collection) {
					Statistics.recordLoad(collection);
				}
			});
		}

		private static final LinkedList<Long> ticks = new LinkedList<>();
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

		private static void doTick() {
			try {
				Thread.sleep(new Random().nextInt(20));
			} catch (InterruptedException ex) {
			}
		}

		public static long getTick() {
			return tick;
		}
	}
}
