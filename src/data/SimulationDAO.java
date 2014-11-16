package data;

import java.util.List;

import data.impl.SimulationCriteria;
import base.Simulation;

public interface SimulationDAO {
	public boolean saveSimulation(Simulation simulation);
	public Simulation getSimulation(Integer id);
	public Simulation getSimulationByName(String name);
	public boolean removeSimulation(Integer id);
	public List<Simulation> findSimulationBy(SimulationCriteria criteria);
}
