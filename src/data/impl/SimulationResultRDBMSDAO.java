package data.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import base.SimulationResult;
import data.Datastore;
import data.SimulationResultDAO;

public class SimulationResultRDBMSDAO extends BaseDAO implements SimulationResultDAO, SimulationResultSQL, SimulationResultDAOConstants {
	private Datastore dataStore;

	public SimulationResultRDBMSDAO() {
		dataStore = JavaDBDatastore.getInstance();
	}

	@Override
	public SimulationResult getSimulationResult(int simulationId, long simulationTime) {
		return null;
	}

	@Override
	public boolean addSimulationResult(int simulationId, int simulationStepId, SimulationResult simulationResult) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		try {
			stmnt = conn.prepareStatement(INSERT);
			int updatedCount = 0;
			int cols = simulationResult.getColumnCount();
			int rows = simulationResult.getRowCount();
			for (short x = 0; x < cols; x++) {
				for (short y = 0; y < rows; y++) {
					stmnt.setInt(1, simulationId);
					stmnt.setInt(2, simulationStepId);
					stmnt.setShort(3, x);
					stmnt.setShort(4, y);
//					stmnt.setDouble(5, simulationResult.getLongitude(x, y));
//					stmnt.setDouble(6, simulationResult.getLatitude(x, y));
					stmnt.setDouble(7, simulationResult.getTemperature(x, y));
					updatedCount += stmnt.executeUpdate();
				}
			}

			return updatedCount > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmnt);
		}
		
		return false;
	}

	@Override
	public boolean removeSimulationResult(int simulationId, long simulationTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SimulationResult> getAllForSimulation(int simulationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAllForSimulation(int simulationId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SimulationResult> findForTimeRange(int simulationId,
			long simulationTimeStart, long simulationTimeStop) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
