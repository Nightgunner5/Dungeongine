package dungeongine.api.event.entity;

import dungeongine.api.entity.Entity;
import dungeongine.api.event.DataChangedEvent;

/** Fired when a property of an {@link dungeongine.api.entity.Entity} is changed. */
public interface EntityDataChangedEvent<T> extends DataChangedEvent<T> {
	/** Gets the entity that had a property change. */
	Entity getEntity();
}
