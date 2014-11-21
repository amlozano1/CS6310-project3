package data.impl;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.ObjectFactory;
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
	
	@Test
	public void testMultipleThreads(){
		Runnable r = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					ObjectFactory.getSimulationDAO().getSimulationNames();
					if(i%10 == 0 && "TestThread-14".equals(Thread.currentThread().getName()) ){
						try {
							System.out.println(Thread.currentThread().getName() + " - Closed Connection");
							JavaDBDatastore.getInstance().getConnection().close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				System.out.println(Thread.currentThread().getName() + " - COMPLETE");
			}
		};
		
		for (int i = 0; i < 25; i++) {
			Thread t = new Thread(r, "TestThread-"+i);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
