package data;

import java.util.Arrays;

import mock.MockSimulationDAOFatory;
import data.impl.SimulationRDBMSDAOFactoryImpl;

public abstract class SimulationDAOFactory {
	public enum DaoType{
		RDBMS,FLATFILE,MOCK
	}
	public abstract SimulationDAO getSimulationDAO();
	public abstract SimulationStepDAO getSimulationStepDAO();
	public abstract SimulationResultDAO getSimulationResultDAO();
	
	public static final SimulationDAOFactory getSimualtionDAOFactory(DaoType type){
		switch (type) {
		case RDBMS:
			return new SimulationRDBMSDAOFactoryImpl();
		case FLATFILE:
			throw new UnsupportedOperationException("Dao type FLATFILE has not been implemented");
		case MOCK:
			return new MockSimulationDAOFatory();
		default:
			throw new IllegalArgumentException("Invalid dao type requested: Supported[" + Arrays.toString(DaoType.values()) + "]");
		}
	}
}
