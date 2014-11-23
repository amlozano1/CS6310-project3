package base;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.ArgumentInvalidException;

public class PersistenceManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveResult() {

		try {
			Utils.parseArguments(new String[]{"-t", "45"});

			PersistenceManager manager = new PersistenceManager();
			
			for (int i = 0; i < 100; i++) {
				manager.saveResult(null, null);
			}
		} catch (ArgumentInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

}
