package dungeongine.apiimpl.event;

import dungeongine.api.entity.Entity;
import dungeongine.api.event.DataChangedEvent;
import dungeongine.api.map.Tile;
import dungeongine.apiimpl.StorageImpl;
import dungeongine.apiimpl.entity.EntityImpl;
import dungeongine.apiimpl.event.entity.EntityDataChangedEventImpl;
import dungeongine.apiimpl.event.map.TileDataChangedEventImpl;
import dungeongine.apiimpl.map.TileImpl;

public class DataChangedEventImpl<T> implements DataChangedEvent<T> {
	private final String fieldName;
	private final T oldValue;
	private final T newValue;

	public DataChangedEventImpl(String fieldName, T oldValue, T newValue) {
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public T getOldValue() {
		return oldValue;
	}

	@Override
	public T getNewValue() {
		return newValue;
	}

	public static <T> DataChangedEvent<T> getInstance(StorageImpl storage, String name, T oldValue, T newValue) {
		if (storage instanceof EntityImpl) {
			return new EntityDataChangedEventImpl<>((Entity) storage, name, oldValue, newValue);
		}
		if (storage instanceof TileImpl) {
			return new TileDataChangedEventImpl<>((Tile) storage, name, oldValue, newValue);
		}
		return new DataChangedEventImpl<>(name, oldValue, newValue);
	}
}
