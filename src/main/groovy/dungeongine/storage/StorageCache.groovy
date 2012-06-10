package dungeongine.storage

import com.google.common.cache.Cache
import groovy.transform.PackageScope
import groovy.transform.EqualsAndHashCode
import com.google.common.cache.CacheBuilder
import groovy.transform.TupleConstructor

@PackageScope
class StorageCache implements IStorage {
	private final IStorage backing

	StorageCache(IStorage backing) {
		this.backing = backing
	}

	private final Cache<CacheKey, Object> cache = CacheBuilder.newBuilder().softValues().build()

	@Override
	def <T> T load(Class<T> type, String identifier) {
		def cached = cache.getIfPresent(new CacheKey(type, identifier))
		if (cached != null)
			return cached as T
		def loaded = backing.load(type, identifier)
		cache.put new CacheKey(type, identifier), loaded
		return loaded
	}

	@Override
	void save(String identifier, Object object) {
		cache.put new CacheKey(object.class, identifier), object
		backing.save(identifier, object)
	}

	@Override
	def <T> List<T> getAll(Class<T> type) {
		return backing.getAll(type)
	}

	@EqualsAndHashCode
	class CacheKey {
		Class<?> type
		String identifier

		CacheKey(Class<?> type, String identifier) {
			this.type = type
			this.identifier = identifier
		}
	}
}
