package dungeongine.storage

import groovy.json.JsonBuilder
import groovy.sql.Sql
import groovy.json.JsonSlurper
import groovy.transform.PackageScope

/**
 * Storage implementation that uses SQLite as a backend. Data is converted to JSON before being put in the database.
 */
@PackageScope
class SqlStorage implements IStorage {
	private final Sql db = Sql.newInstance("jdbc:sqlite:dungeongine.db", "org.sqlite.JDBC")

	@Override
	def <T> T load(Class<T> type, String identifier) {
		def data = db.firstRow("select data from storage where type = ? and identifier = ?", [type.name, identifier])
		def obj = type.newInstance()
		if (data != null)
			new JsonSlurper().parseText(data.data).each {key, value -> obj.putAt(key, value)}
		return obj
	}

	@Override
	void save(String identifier, Object object) {
		db.execute("create table if not exists storage ( type text, identifier text primary key, data blob )")
		db.executeInsert("insert or replace into storage (type, identifier, data) values(?, ?, ?)", [object.class.name, identifier, new JsonBuilder(object).toString()])
	}

	@Override
	def <T> List<T> getAll(Class<T> type) {
		List<T> items = []
		db.rows("select identifier from storage where type = ?", [type.name]).each {row -> items.add load(type, row.identifier as String)}
		return items
	}
}
