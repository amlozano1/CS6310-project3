package data;

import java.util.Arrays;

import data.impl.SimulationRDBMSDAOFactoryImpl;

public abstract class SimualtionDAOFactory {
	public enum DaoType{
		RDBMS,FLATFILE
	}
	public abstract SimulationDAO getSimulationDAO();
	public abstract SimulationResultDAO getSimulationResultDAO();
	
	public static final SimualtionDAOFactory getSimualtionDAOFactory(DaoType type){
		switch (type) {
		case RDBMS:
			return new SimulationRDBMSDAOFactoryImpl();
		case FLATFILE:
			throw new UnsupportedOperationException("Dao type FLATFILE has not been implemented");
		default:
			throw new IllegalArgumentException("Invalid dao type requested: Supported[" + Arrays.toString(DaoType.values()) + "]");
		}
	}
}
