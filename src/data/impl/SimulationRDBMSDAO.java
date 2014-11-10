package data.impl;

import base.Simulation;
import data.Datastore;
import data.SimulationDAO;

public class SimulationRDBMSDAO implements SimulationDAO {
	private Datastore dataStore;
	
	public SimulationRDBMSDAO() {
		dataStore = JavaDBDatastore.getInstance();
	}
	
	@Override
	public Simulation getSimulation() {
		// TODO Auto-generated method stub
		return null;
	}
}
