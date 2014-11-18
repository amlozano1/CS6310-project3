package data;

import base.SimulationResult;

public interface CellDAO {
	
	public void saveCells(int simulationId, int simulationResultId, SimulationResult cells);
}
