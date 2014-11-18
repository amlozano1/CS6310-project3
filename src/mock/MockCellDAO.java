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
	public boolean removeCellsForSimulationResult(int simulationResultId) {
		// TODO Auto-generated method stub
		return false;
	}
}
