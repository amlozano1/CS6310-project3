package data.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.ObjectFactory;
import base.SimulationResult;
import data.SimulationDAO;
import data.SimulationResultDAO;

public class SimulationResultRDBMSDAOTest {
	private static final String SIMULATION_NAME = "SIMULATIONRESULT_TEST";
	
	private SimulationResultDAO dao;
	private SimulationDAO simulationDAO;

	private Integer simulationId;
	private Integer simulationResultId;
	
	@Before
	public void setUp() throws Exception {
		simulationDAO = ObjectFactory.getSimulationDAO();
		simulationId = simulationDAO.saveSimulation(TestHelper.createSimulation(SIMULATION_NAME));

		dao = ObjectFactory.getSimulationResultDAO();
	}

	@After
	public void tearDown() throws Exception {
		dao.removeSimulationResult(simulationResultId);
		simulationDAO.removeSimulation(simulationId);
	}

	@Test
	public void testGetSimulationResult() {
		simulationResultId = dao.addSimulationResult(simulationId, create());
		
		SimulationResult result = dao.getSimulationResult(simulationResultId);
		assertNotNull("Result not returned:", result);
		assertEquals("Result data not correct:", result.getRowCount(), 10);
		assertEquals("Result data not correct:", result.getColumnCount(), 10);
	}

	@Test
	public void testAddSimulationResult() {
		simulationResultId = dao.addSimulationResult(simulationId, create());
		assertNotNull("Not successful:",  simulationResultId);
	}

	@Test
	public void testRemoveSimulationResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllForSimulation() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveAllForSimulation() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindForTimeRange() {
		fail("Not yet implemented");
	}

	private SimulationResult create(){
		return TestHelper.createSimulationResult();
	}
}
