package data;

import base.Cell;
import base.SimulationResult;

public interface CellDAO {
	
	public void saveCells(int simulationId, int simulationResultId, SimulationResult cells);
	public Cell[][] getCellsForSimulationResult(int simulationResultId);
	public boolean removeAllForSimulationResult(int simulationResultId);
	public boolean removeAllForSimulation(int simulationId);
	public boolean saveCell(int simulationId, int simulationResultId, Cell cell, int row, int column);
}
