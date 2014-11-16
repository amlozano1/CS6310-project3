package data.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.SimulationResultDAO;
import base.Cell;
import base.ObjectFactory;
import base.SimulationResult;

public class SimulationResultRDBMSDAOTest {
	private SimulationResultDAO dao;
	
	@Before
	public void setUp() throws Exception {
		dao = ObjectFactory.getSimulationResultDAO();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSimulationResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddSimulationResult() {
		fail("Not yet implemented");

//		dao.addSimulationResult(simulationId, simulationStepId, simulationResult);
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
		Cell[][] resultData = new Cell[10][10];
//		for(int x = 1; x<=10; x++){
//			for (int y = 1; y <= 10; y++) {
//				Cell cell = new Cell();
//				cell.setLongitude(x * 100);
//				cell.setLatitude(y);
//				cell.setTemperture(x*y);
//			}
//		}
		return ObjectFactory.getInitialGrid(10, 10);
	}
}
