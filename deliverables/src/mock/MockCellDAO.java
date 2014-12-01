package mock;

import base.Cell;
import base.SimulationResult;
import data.CellDAO;

public class MockCellDAO implements CellDAO {

	@Override
	public void saveCells(int simulationId, int simulationResultId,
			SimulationResult cells) {
		// TODO Auto-generated method stub

	}

	@Override
	public Cell[][] getCellsForSimulationResult(int simulationResultId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAllForSimulation(int simulationId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAllForSimulationResult(int simulationResultId) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean saveCell(int simulationId, int simulationResultId,
			double temp, double lon, double lat, int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean saveCell(int simulationId, int simulationResultId,
			Cell cell, int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}
}
