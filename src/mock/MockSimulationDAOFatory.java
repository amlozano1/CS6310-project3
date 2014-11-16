package mock;

import data.SimualtionDAOFactory;
import data.SimulationDAO;
import data.SimulationResultDAO;

public class MockSimulationDAOFatory extends SimualtionDAOFactory {
	@Override
	public SimulationDAO getSimulationDAO() {
		return new MockSimulationDAO();
	}

	@Override
	public SimulationResultDAO getSimulationResultDAO() {
		return new MockSimulationResultDAO();
	}
}
