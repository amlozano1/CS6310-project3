package mock;

import java.util.List;

import base.SimulationResult;
import data.SimulationResultDAO;

public class MockSimulationResultDAO implements SimulationResultDAO {

	@Override
	public SimulationResult getSimulationResult(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer addSimulationResult(int simulationId,
			SimulationResult simulationResult) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeSimulationResult(int id) {
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
	
	@Override
	public SimulationResult findSimulationResult(int simulationId,
			long simulationTime) {
		// TODO Auto-generated method stub
		return null;
	}
}
