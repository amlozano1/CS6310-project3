package mock;

import java.util.List;

import base.SimulationResult;
import data.SimulationResultDAO;

public class MockSimulationResultDAO implements SimulationResultDAO {

	@Override
	public SimulationResult getSimulationResult(int simulationId,
			long simulationTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addSimulationResult(int simulationId, int simulationStepId,
			SimulationResult simulationResult) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeSimulationResult(int simulationId, long simulationTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SimulationResult> getAllForSimulation(int simulationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAllForSimulation(int simulationId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SimulationResult> findForTimeRange(int simulationId,
			long simulationTimeStart, long simulationTimeStop) {
		// TODO Auto-generated method stub
		return null;
	}
}
