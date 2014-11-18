package data;

import java.util.List;

import base.SimulationResult;

public interface SimulationResultDAO {
	public SimulationResult getSimulationResult(int id);
	public Integer addSimulationResult(int simulationId, SimulationResult simulationResult);
	public boolean removeSimulationResult(int id);

	public List<SimulationResult> getAllForSimulation(int simulationId);
	public boolean removeAllForSimulation(int simulationId);

	public List<SimulationResult> findForTimeRange(int simulationId, long simulationTimeStart, long simulationTimeStop);
}