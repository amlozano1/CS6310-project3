package data;

import java.util.List;

import base.SimulationResult;

public interface SimulationResultDAO {
	public SimulationResult getSimulationResult(int simulationId, long simulationTime);
	public void addSimulationResult(int simulationId, long simulationTime, SimulationResult simulationResult);
	public void removeSimulationResult(int simulationId, long simulationTime);

	public List<SimulationResult> getAllForSimulation(int simulationId);
	public void removeAllForSimulation(int simulationId);

	public List<SimulationResult> findForTimeRange(int simulationId, long simulationTimeStart, long simulationTimeStop);
}