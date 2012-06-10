package dungeongine.storage

class Storage {
	private static IStorage instance
	/**
	 * Gets an instance of {@link IStorage}.
	 */
	static IStorage getInstance() {
		init()
		instance
	}

	/**
	 * For testing only
	 */
	static void forceNoop() {
		if (instance != null)
			return
		instance = new NoopStorage()
	}

	static void init() {
		if (instance != null)
			return
		instance = new StorageCache(new SqlStorage())
	}
}
