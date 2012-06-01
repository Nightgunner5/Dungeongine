package dungeongine.apiimpl;

import com.google.common.base.Objects;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import dungeongine.Main;
import dungeongine.api.Dungeongine;
import dungeongine.api.Events;
import dungeongine.api.Storage;
import dungeongine.apiimpl.event.DataChangedEventImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public abstract class StorageImpl implements Storage {
	private final String collection;
	private final String identifier;
	private final DBObject query;

	private static final Set<StorageImpl> instances = Sets.newSetFromMap(new MapMaker().weakKeys().<StorageImpl, Boolean>makeMap());
	private boolean dirty;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (StorageImpl instance : instances) {
					instance.save();
				}
				Database.shutdown();
			}
		});
	}

	public static void init() {
		// Yay for <clinit>
	}

	public StorageImpl(String collection, String identifier) {
		this.collection = collection;
		this.identifier = identifier;
		query = new BasicDBObject().append("identifier", identifier);
		Map<String, Object> data = getData();
		if (data == null) {
			data = getDefault();
			dirty = true;
		}
		load(data);
		instances.add(this);
	}

	@Override
	protected void finalize() throws Throwable {
		save();
	}

	protected abstract void load(Map<String, Object> data);

	protected abstract Map<String, Object> getDefault();

	protected abstract void serialize(Map<String, Object> data);

	private static final DB database;

	static {
		Database.startup();
		try {
			Mongo mongo = new Mongo(InetAddress.getLoopbackAddress().getHostAddress(), Main.DB_PORT);
			database = mongo.getDB("dungeongine");
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private Map<String, Object> getData() {
		DBObject data = database.getCollection(collection).findOne(query);

		if (statistics != null)
			statistics.recordLoad(collection);
		else
			Logger.getLogger(StorageImpl.class.getName()).warning("Statistics not registered");

		return data == null ? null : data.toMap();
	}

	protected final <T> void dataChanged(String name, T oldValue, T newValue) {
		if (!Objects.equal(oldValue, newValue)) {
			Events.dispatch(DataChangedEventImpl.getInstance(this, name, oldValue, newValue));
			dirty = true;
		}
	}

	public void save() {
		if (!dirty)
			return;

		database.getCollection(collection).remove(query);

		BasicDBObject data = new BasicDBObject().append("identifier", identifier);
		serialize(data);

		database.getCollection(collection).insert(data);

		dirty = false;

		if (statistics != null)
			statistics.recordSave(collection);
		else
			Logger.getLogger(StorageImpl.class.getName()).warning("Statistics not registered");
	}

	private static Statistics statistics;

	public static void register(Statistics statistics) {
		StorageImpl.statistics = statistics;
	}

	static {
		// Register it
		Dungeongine.Statistics.getSaves(true);
	}

	public static interface Statistics {
		void recordSave(String collection);

		void recordLoad(String collection);
	}
}
