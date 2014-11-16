package data;

import java.util.List;

import base.SimulationResult;

public interface SimulationResultDAO {
	public SimulationResult getSimulationResult(int simulationId, long simulationTime);
	public boolean addSimulationResult(int simulationId, int simulationStepId, SimulationResult simulationResult);
	public boolean removeSimulationResult(int simulationId, long simulationTime);

	public List<SimulationResult> getAllForSimulation(int simulationId);
	public boolean removeAllForSimulation(int simulationId);

	public List<SimulationResult> findForTimeRange(int simulationId, long simulationTimeStart, long simulationTimeStop);
}