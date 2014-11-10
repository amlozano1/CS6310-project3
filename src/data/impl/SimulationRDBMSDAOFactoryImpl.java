package data.impl;

import data.SimualtionDAOFactory;
import data.SimulationDAO;
import data.SimulationResultDAO;

public class SimulationRDBMSDAOFactoryImpl extends SimualtionDAOFactory {
	
	@Override
	public SimulationDAO getSimulationDAO() {
		return new SimulationRDBMSDAO();
	}

	@Override
	public SimulationResultDAO getSimulationResultDAO() {
		return new SimulationResultRDBMSDAO();
	}

}
