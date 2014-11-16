package mock;

import java.util.List;

import base.Simulation;
import base.SimulationParameters;
import data.SimulationDAO;
import data.impl.SimulationCriteria;

public class MockSimulationDAO implements SimulationDAO {

	@Override
	public boolean saveSimulation(Simulation simulation) {
		return true;
	}

	@Override
	public Simulation getSimulation(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Simulation getSimulationByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeSimulation(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Simulation> findSimulationBy(SimulationCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}
}
