package dungeongine.storage

import groovy.transform.PackageScope

@PackageScope
class NoopStorage implements IStorage {
	private static final Map<Class<?>, Map<String, ?>> storage = [:]

	@Override
	def <T> T load(Class<T> type, String identifier) {
		if (type in storage)
			if (identifier in storage[type])
				return storage[type][identifier] as T
		return type.newInstance()
	}

	@Override
	void save(String identifier, Object object) {
		if (!(object.class in storage))
			storage[object.class] = [:]
		storage[object.class][identifier] = object
	}
}
