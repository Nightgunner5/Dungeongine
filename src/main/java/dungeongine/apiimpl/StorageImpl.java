package dungeongine.apiimpl;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import dungeongine.api.Events;
import dungeongine.apiimpl.event.DataChangedEventImpl;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class StorageImpl {
	private final String identifier;

	public StorageImpl(String identifier) {
		this.identifier = CharMatcher.anyOf("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-.,").negate().removeFrom(identifier);
		load(getData());
	}

	@Override
	protected void finalize() throws Throwable {
		save();
	}

	protected abstract void load(Map<String, Serializable> data);

	protected abstract Map<String, Serializable> getDefault();

	protected abstract void serialize(Map<String, Serializable> data);

	private Map<String, Serializable> getData() {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(new File(SAVE_DIR, identifier)));
			Preconditions.checkState(identifier.equals(in.readObject()));
			return (Map<String, Serializable>) in.readObject();
		} catch (Exception ex) {
			return getDefault();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException ex) {
				Logger.getLogger(StorageImpl.class.getName()).log(Level.WARNING, String.format("Exception when closing storage input for '%s'.", identifier), ex);
			}
		}
	}

	protected <T> void dataChanged(String name, T oldValue, T newValue) {
		Events.dispatch(new DataChangedEventImpl<>(name, oldValue, newValue));
	}

	private static final File SAVE_DIR = new File(System.getProperty("user.home"), ".dungeongine-save");

	static {
		SAVE_DIR.mkdirs();
	}

	public void save() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(SAVE_DIR, identifier)));
		try {
			out.writeObject(identifier);
			Map<String, Serializable> map = Maps.newLinkedHashMap();
			serialize(map);
			out.writeObject(map);
		} finally {
			out.close();
		}
	}
}
