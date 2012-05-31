package dungeongine.apiimpl;

import com.google.common.base.Objects;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.orientechnologies.orient.core.command.OCommandRequest;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import dungeongine.api.Events;
import dungeongine.apiimpl.event.DataChangedEventImpl;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public abstract class StorageImpl {
	private final String identifier;

	private static final Set<StorageImpl> instances = Sets.newSetFromMap(new MapMaker().weakKeys().<StorageImpl, Boolean>makeMap());

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (StorageImpl instance : instances) {
					instance.save();
				}
			}
		});
	}

	public StorageImpl(String identifier) {
		this.identifier = identifier;
		load(getData());
		instances.add(this);
	}

	@Override
	protected void finalize() throws Throwable {
		save();
	}

	protected abstract void load(Map<String, Object> data);

	protected abstract Map<String, Object> getDefault();

	protected abstract void serialize(Map<String, Object> data);

	private static final ODatabaseDocumentTx database;

	static {
		try {
			database = new ODatabaseDocumentTx("local:" + new File("dungeongine.sav").getCanonicalPath());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static final OCommandRequest query = database.command(new OSQLSynchQuery<ODocument>("SELECT * FROM storage WHERE identifier = ?"));


	private Map<String, Object> getData() {
		ODocument data;
		synchronized (database) {
			data = query.execute(identifier);
		}
		if (data != null)
			return convert(data);
		return getDefault();
	}

	private Map<String, Object> convert(ODocument data) {
		Map<String, Object> map = Maps.newLinkedHashMap();
		for (Map.Entry<String, Object> entry : data) {
			map.put(entry.getKey(), entry.getValue() instanceof ODocument ? convert((ODocument) entry.getValue()) : entry.getValue());
		}
		return map;
	}

	protected <T> void dataChanged(String name, T oldValue, T newValue) {
		if (!Objects.equal(oldValue, newValue))
			Events.dispatch(new DataChangedEventImpl<>(name, oldValue, newValue));
	}

	public void save() {
		synchronized (database) {
			ODocument data = query.execute(identifier);
			if (data == null)
				data = new ODocument("storage");
			data.clear();
			Map<String, Object> serialized = Maps.newLinkedHashMap();
			serialize(serialized);
			for (Map.Entry<String, Object> entry : serialized.entrySet()) {
				data.field(entry.getKey(), entry.getValue() instanceof Map<?, ?> ? convert((Map<String, Object>) entry.getValue()) : entry.getValue());
			}
			data.save();
		}
	}

	private ODocument convert(Map<String, Object> map) {
		ODocument data = new ODocument();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			data.field(entry.getKey(), entry.getValue() instanceof Map<?, ?> ? convert((Map<String, Object>) entry.getValue()) : entry.getValue());
		}
		return data;
	}
}
