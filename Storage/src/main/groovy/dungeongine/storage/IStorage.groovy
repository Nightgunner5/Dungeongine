package dungeongine.storage

public interface IStorage {
	public <T> T load(Class<T> type, String identifier)
	public void save(String identifier, Object object)
}