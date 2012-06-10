package dungeongine.storage

/**
 * The methods available in any implementation of storage. Classes in method parameters must have a visible
 * default constructor.
 */
public interface IStorage {
	/**
	 * Retrieves an object from the persistent storage. If the object is not found, a new instance of the class
	 * will be returned.
	 */
	public <T> T load(Class<T> type, String identifier)

	/**
	 * Saves an object in the persistent storage. If another object is already in the storage with the same class
	 * and identifier, it is replaced.
	 */
	public void save(String identifier, Object object)

	/**
	 * Gets a list of all objects of a given class in the persistent storage.
	 */
	public <T> List<T> getAll(Class<T> type)
}