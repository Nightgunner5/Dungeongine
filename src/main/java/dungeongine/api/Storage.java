package dungeongine.api;

/**
 * Represents an object that is persistent. Classes implementing this interface must load data when an instance of the
 * class is created and save data when the instance is garbage collected and when the game closes.
 */
public interface Storage {
	/** Force the object to save immediately. Implementations may ignore this if the object has not changed. */
	void save();
}
