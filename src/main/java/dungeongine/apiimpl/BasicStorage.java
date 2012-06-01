package dungeongine.apiimpl;

import java.util.Collections;
import java.util.Map;

public class BasicStorage<T> extends StorageImpl {
	private T data;

	public BasicStorage(String identifier) {
		super("basic", identifier);
	}

	protected BasicStorage(String collection, String identifier) {
		super(collection, identifier);
	}

	protected BasicStorage(String collection, String identifier, boolean requireExists) {
		super(collection, identifier, requireExists);
	}

	public T getData() {
		return data;
	}

	public BasicStorage<T> setDefault(T data) {
		if (this.data == null)
			setData(data);
		return this;
	}

	public void setData(T data) {
		dataChanged("data", this.data, data);
		this.data = data;
	}

	@Override
	protected void load(Map<String, Object> data) {
		this.data = (T) data.get("data");
	}

	@Override
	protected Map<String, Object> getDefault() {
		return Collections.emptyMap();
	}

	@Override
	protected void serialize(Map<String, Object> data) {
		data.put("data", this.data);
	}
}
