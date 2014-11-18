package data.impl;

import data.SimulationDAOFactory;
import data.SimulationDAO;
import data.SimulationResultDAO;
import data.CellDAO;

public class SimulationRDBMSDAOFactoryImpl extends SimulationDAOFactory {

	@Override
	public SimulationDAO getSimulationDAO() {
		return new SimulationRDBMSDAO();
	}

	@Override
	public SimulationResultDAO getSimulationResultDAO() {
		return new SimulationResultRDBMSDAO();
	}

	@Override
	public CellDAO getCellDAO() {
		return new CellRDBMSDAO();
	}
}
