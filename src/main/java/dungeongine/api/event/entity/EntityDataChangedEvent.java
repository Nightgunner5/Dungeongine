package dungeongine.api.event.entity;

import dungeongine.api.entity.Entity;
import dungeongine.api.event.DataChangedEvent;

public interface EntityDataChangedEvent<T> extends DataChangedEvent<T> {
	Entity getEntity();
}
