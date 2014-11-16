package data.impl;

import static org.junit.Assert.*;

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
		boolean result = dao.saveSimulation(create());
		assertTrue("Insert failed:", result);
	}
	
	@Test
	public void testGet() {
		Simulation expected = create();
		SimulationParameters parameters = expected.getSimulationParameters();
		
		Simulation fromDb = dao.getSimulation(SIMULATION_NAME);
		assertNotNull("Simulation not found:", fromDb);
		assertEquals("Name does not match:", expected.getName(), fromDb.getName());
		assertNotNull("SimualtionParameters missing:", fromDb.getSimulationParameters());
		assertEquals("Parameters do not match:", parameters, fromDb.getSimulationParameters());
	}
	
	@Test
	public void testRemove() {
		boolean result = dao.removeSimulation(SIMULATION_NAME);
		assertTrue("Delete failed:", result);
	}

	private Simulation create(){
		Simulation simulation = new Simulation();
		simulation.setName(SIMULATION_NAME);
		
		SimulationParameters parameters = new SimulationParameters();
		parameters.setAxialTilt(23.44d);
		parameters.setGridSpacing((short)15);
		parameters.setLength((short)12);
		parameters.setOrbitalEccentricity(.0167d);
		parameters.setTimeStep(1440);
		simulation.setSimulationParameters(parameters);
		
		return simulation;
	}
}
