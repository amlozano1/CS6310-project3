package data;

import java.util.List;

import base.Simulation;

public interface SimulationDAO {
	public boolean saveSimulation(Simulation simulation);
	public Simulation getSimulation(String name);
	public boolean removeSimulation(String name);
	public List<Simulation> findSimulationBy();
}
