package dungeongine.api.event;

public interface DataChangedEvent<T> extends Event {
	String getFieldName();
	T getOldValue();
	T getNewValue();
}
