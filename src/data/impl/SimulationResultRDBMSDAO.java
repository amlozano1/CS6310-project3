package data.impl;

import base.SimulationResult;
import data.Datastore;
import data.SimulationResultDAO;

public class SimulationResultRDBMSDAO implements SimulationResultDAO {
	private Datastore dataStore;

	public SimulationResultRDBMSDAO() {
		dataStore = JavaDBDatastore.getInstance();
	}
	
	@Override
	public SimulationResult getSimulationResult() {
		// TODO Auto-generated method stub
		return null;
	}
}
