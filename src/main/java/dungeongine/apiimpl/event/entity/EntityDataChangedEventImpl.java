package dungeongine.apiimpl.event.entity;

import dungeongine.api.entity.Entity;
import dungeongine.api.event.entity.EntityDataChangedEvent;
import dungeongine.apiimpl.event.DataChangedEventImpl;

public class EntityDataChangedEventImpl<T> extends DataChangedEventImpl<T> implements EntityDataChangedEvent<T> {
	private final Entity entity;

	public EntityDataChangedEventImpl(Entity entity, String fieldName, T oldValue, T newValue) {
		super(fieldName, oldValue, newValue);
		this.entity = entity;
	}

	@Override
	public Entity getEntity() {
		return entity;
	}
}
