package data.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import base.Cell;
import base.ObjectFactory;
import data.CellDAO;

public class CellRDBMSDAOTest {
	
	private CellDAO dao;
	private int simulationId;
	private int simulationResultId;
	
	@Before
	public void setUp() throws Exception {
		simulationId = ObjectFactory.getSimulationDAO().saveSimulation(TestHelper.createSimulation("UNIT_TESTING_CELLDAO"));
		simulationResultId = ObjectFactory.getSimulationResultDAO().addSimulationResult(simulationId, TestHelper.createSimulationResult());
		dao = ObjectFactory.getCellDAO();
	}

	@After
	public void tearDown() throws Exception {
		ObjectFactory.getSimulationResultDAO().removeAllForSimulation(simulationId);
		ObjectFactory.getSimulationDAO().removeSimulation(simulationId);
	}

	@Test @Ignore
	public void testSaveCells() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCellsForSimulationResult() {
		Cell[][] data = dao.getCellsForSimulationResult(simulationResultId);
		assertNotNull("No cell data returned:", data);
		assertEquals("Cell row data not valid:", 10, data.length);
		assertEquals("Cell column data not valid:", 10, data[0].length);
	}

	@Test
	public void testSaveCell() {
		Cell cell = new Cell();
		cell.setLatitude(45d);
		cell.setLongitude(54d);
		cell.setTemperature(100d);
		
		boolean success = dao.saveCell(simulationId, simulationResultId, cell, 0, 0);
		assertTrue("Save failed:", success);
	}
}
