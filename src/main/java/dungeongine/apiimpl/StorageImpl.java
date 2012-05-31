package dungeongine.apiimpl;

import com.google.common.base.Objects;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;
import com.mongodb.*;
import de.flapdoodle.embedmongo.MongoDBRuntime;
import de.flapdoodle.embedmongo.MongodExecutable;
import de.flapdoodle.embedmongo.MongodProcess;
import de.flapdoodle.embedmongo.config.MongodConfig;
import de.flapdoodle.embedmongo.distribution.Version;
import de.flapdoodle.embedmongo.runtime.Network;
import dungeongine.Main;
import dungeongine.api.Events;
import dungeongine.api.Storage;
import dungeongine.apiimpl.event.DataChangedEventImpl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Map;
import java.util.Set;

public abstract class StorageImpl implements Storage {
	private final String collection;
	private final String identifier;
	private final DBObject query;
	private final boolean inDB;

	private static final Set<StorageImpl> instances = Sets.newSetFromMap(new MapMaker().weakKeys().<StorageImpl, Boolean>makeMap());
	private boolean dirty;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (StorageImpl instance : instances) {
					instance.save();
				}
				try {
					database.getMongo().getDB("admin").command(new BasicDBObject().append("shutdown", 1));
				} catch (MongoException.Network ex) {
					// Expected
				}
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
		inDB = data != null;
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

	private static final MongodProcess mongod;
	private static final DB database;
	static {
		MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
		try {
			MongodExecutable executable = runtime.prepare(new MongodConfig(Version.V2_0, Main.DB_PORT, Network.localhostIsIPv6(), "dungeongine.sav"));
			try {
				Field field = executable.getClass().getDeclaredField("_stopped");
				field.setAccessible(true);
				field.set(executable, true);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			mongod = executable.start();
			Mongo mongo = new Mongo(InetAddress.getLoopbackAddress().getHostAddress(), Main.DB_PORT);
			database = mongo.getDB("dungeongine");
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private Map<String, Object> getData() {
		DBObject data = database.getCollection(collection).findOne(query);
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
	}
}
