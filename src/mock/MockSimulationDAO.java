package mock;

import java.util.List;

import base.Simulation;
import data.SimulationDAO;
import data.impl.SimulationCriteria;

public class MockSimulationDAO implements SimulationDAO {

	@Override
	public Integer saveSimulation(Simulation simulation) {
		// TODO Auto-generated method stub
		return null;
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
