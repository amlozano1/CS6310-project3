package mock;

import data.SimulationDAOFactory;
import data.SimulationDAO;
import data.SimulationResultDAO;
import data.CellDAO;

public class MockSimulationDAOFatory extends SimulationDAOFactory {
	@Override
	public SimulationDAO getSimulationDAO() {
		return new MockSimulationDAO();
	}

	@Override
	public SimulationResultDAO getSimulationResultDAO() {
		return new MockSimulationResultDAO();
	}
	
	@Override
	public CellDAO getCellDAO() {
		// TODO Auto-generated method stub
		return null;
	}
}
