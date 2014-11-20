package data.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.Datastore;

public class JavaDBDatastoreTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReset() {
		Datastore ds = JavaDBDatastore.getInstance();
		ds.reset();
	}
}
