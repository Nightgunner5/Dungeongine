package dungeongine.api.event;

/**
 * Fired when a data storage type such as {@link dungeongine.api.entity.Entity} or {@link dungeongine.api.map.Tile} has
 * a property changed.
 */
public interface DataChangedEvent<T> extends Event {
	/** Gets the name of the field that changed. */
	String getFieldName();

	/** Gets the old value of the field. */
	T getOldValue();

	/** Gets the new value of the field. */
	T getNewValue();
}
