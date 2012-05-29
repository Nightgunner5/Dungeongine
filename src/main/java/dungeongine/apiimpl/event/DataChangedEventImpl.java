package dungeongine.apiimpl.event;

import dungeongine.api.event.DataChangedEvent;

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
}
