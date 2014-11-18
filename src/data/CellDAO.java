package data;

import base.Cell;
import base.SimulationResult;

public interface CellDAO {
	
	public void saveCells(int simulationId, int simulationResultId, SimulationResult cells);
	public Cell[][] getCellsForSimulationResult(int simulationResultId);
	public boolean removeCellsForSimulationResult(int simulationResultId);
}
