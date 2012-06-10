package dungeongine.storage

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

class SqlStorageTest extends GroovyTestCase {
	void testSqlStorage() {
		def storage = new SqlStorage()
		def obj = new TestObject(a: 'B', c: 0, e: new TestObject(a: 'b', c: 0xD))
		storage.save('abc', obj)
		assert obj == storage.load(TestObject.class, 'abc')
	}

	@EqualsAndHashCode
	@ToString
	private static class TestObject {
		String a
		int c
		TestObject e
	}
}
