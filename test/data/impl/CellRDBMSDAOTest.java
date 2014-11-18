package data.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.Cell;
import base.ObjectFactory;
import data.CellDAO;

public class CellRDBMSDAOTest {
	
	private CellDAO dao;

	@Before
	public void setUp() throws Exception {
		dao = ObjectFactory.getCellDAO();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveCells() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCellsForSimulationResult() {
		Cell[][] data = dao.getCellsForSimulationResult(301);
		assertNotNull("No cell data returned:", data);
	}

}
