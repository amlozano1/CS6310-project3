package mock;

import java.util.List;

import base.Simulation;
import base.SimulationParameters;
import data.SimulationDAO;

public class MockSimulationDAO implements SimulationDAO {

	@Override
	public boolean saveSimulation(Simulation simulation) {
		return true;
	}

	@Override
	public Simulation getSimulation(String name) {
		Simulation simulation = new Simulation();
		simulation.setName("MOCKED");
		
		SimulationParameters parameters = new SimulationParameters();
		parameters.setAxialTilt(23.44d);
		parameters.setGridSpacing((short)15);
		parameters.setLength((short)12);
		parameters.setOrbitalEccentricity(.0167d);
		parameters.setTimeStep(1440);
		
		simulation.setSimulationParameters(parameters);
		
		return simulation;
	}

	@Override
	public boolean removeSimulation(String name) {
		return true;
	}

	@Override
	public List<Simulation> findSimulationBy() {
		// TODO Auto-generated method stub
		return null;
	}

}
