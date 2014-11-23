package base;

import base.Utils.InvocationParms;
import data.CellDAO;
import data.SimulationResultDAO;

public class PersistenceManager {
	private short gPrecision;
	private short tPrecision;
	
	private int savedCount;
	private int totalCount;
	
	private SimulationResultDAO resultDAO = ObjectFactory.getSimulationResultDAO();
	private CellDAO cellDAO = ObjectFactory.getCellDAO();
	private InvocationParms invocationParms ;
	
	public PersistenceManager() {
		invocationParms = (InvocationParms)System.getProperties().get(Utils.INVOCATION_PARAMETERS_KEY);
		tPrecision = invocationParms.temporalPrecision;
		gPrecision = invocationParms.geographicPrecision;
		savedCount = 0;
		totalCount = 0;
	}
	
	public void saveResult(Simulation simulation, SimulationResult result){
		totalCount++;
		int simulationId = simulation.getId();
		
		if(saveResult()){
			savedCount++;
			int simulationResultId = resultDAO.addSimulationResult(simulation.getId(), result);
			Cell[][] data = result.getResultData();
			saveCells(simulationId, simulationResultId, data);
		}
	}

	public void saveCells(int simulationId, int simulationResultId, Cell[][] data) {
		int cellsSaved = 0;
		int totalCells = 0;
		
		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data[x].length; y++) {
				float ratio = (float)cellsSaved/(float)totalCells;
				if((ratio) * 100 < gPrecision){
					cellDAO.saveCell(simulationId, simulationResultId, data[x][y], x, y);
				}
			}
		}		
	}

	private boolean saveResult() {
		float ratio = (float)savedCount/(float)totalCount;
		return (ratio) * 100 < tPrecision;
	}
}
