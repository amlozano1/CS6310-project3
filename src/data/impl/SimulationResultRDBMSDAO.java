package data.impl;

import java.sql.Connection;
import java.util.List;

import base.SimulationResult;
import data.Datastore;
import data.SimulationResultDAO;

public class SimulationResultRDBMSDAO extends BaseDAO implements SimulationResultDAO, SimulationResultSQL, SimulationResultDAOConstants {
	private Datastore dataStore;

	public SimulationResultRDBMSDAO() {
		dataStore = JavaDBDatastore.getInstance();
	}

	@Override
	public SimulationResult getSimulationResult(int simulationId, long simulationTime) {
		Connection conn = dataStore.getConnection();
		return null;
	}

	@Override
	public void addSimulationResult(int simulationId, long simulationTime, SimulationResult simulationResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSimulationResult(int simulationId, long simulationTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SimulationResult> getAllForSimulation(int simulationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeAllForSimulation(int simulationId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SimulationResult> findForTimeRange(int simulationId,
			long simulationTimeStart, long simulationTimeStop) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
