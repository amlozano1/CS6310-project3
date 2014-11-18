package data.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.ObjectFactory;
import base.Simulation;
import base.SimulationParameters;
import data.SimulationDAO;

public class SimulationRDBMSDAOTest {
	private static final String SIMULATION_NAME = "JUNIT_TEST_SIMULATION";
	private SimulationDAO dao;
	
	@Before
	public void setUp() throws Exception {
		dao = ObjectFactory.getSimulationDAO();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAdd() {
		Integer id = dao.saveSimulation(create());
		assertNotNull("Insert failed:", id);
	}
	
	@Test
	public void testGet() {
		Simulation expected = create();
		SimulationParameters parameters = expected.getSimulationParameters();
		
		Simulation fromDb = dao.getSimulationByName(SIMULATION_NAME);
		assertNotNull("Simulation not found:", fromDb);
		assertEquals("Name does not match:", expected.getName(), fromDb.getName());
		assertNotNull("SimualtionParameters missing:", fromDb.getSimulationParameters());
		assertEquals("Parameters do not match:", parameters, fromDb.getSimulationParameters());
	}
	
	@Test
	public void testFindByName() {
		Simulation expected = create();
		SimulationParameters parameters = expected.getSimulationParameters();
		SimulationCriteria criteria = new SimulationCriteria();
		criteria.withName(SIMULATION_NAME);
		
		List<Simulation> listFromDb = dao.findSimulationBy(criteria);
		assertEquals("Wrong quantity returned:", 1, listFromDb.size());
		
		Simulation fromDb = listFromDb.get(0);
		assertNotNull("Simulation not found:", fromDb);
		assertEquals("Name does not match:", expected.getName(), fromDb.getName());
		assertNotNull("SimualtionParameters missing:", fromDb.getSimulationParameters());
		assertEquals("Parameters do not match:", parameters, fromDb.getSimulationParameters());
		
		criteria.clear();
		criteria.withName(SIMULATION_NAME+"SHOULD_NOT_FIND");
		
		listFromDb = dao.findSimulationBy(criteria);
		assertEquals("Wrong quantity returned:", 0, listFromDb.size());
	}
	
	@Test
	public void testRemove() {
		Simulation testSim = dao.getSimulationByName(SIMULATION_NAME);
		boolean result = dao.removeSimulation(testSim.getId());
		assertTrue("Delete failed:", result);
	}

	private Simulation create(){
		return TestHelper.createSimulation(SIMULATION_NAME);
	}
}
