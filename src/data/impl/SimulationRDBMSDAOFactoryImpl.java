package data.impl;

import data.SimulationDAOFactory;
import data.SimulationDAO;
import data.SimulationResultDAO;
import data.SimulationStepDAO;

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
	public SimulationStepDAO getSimulationStepDAO() {
		return new SimulationStepRDBMSDAO();
	}
}
