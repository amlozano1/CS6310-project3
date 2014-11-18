package data.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import base.SimulationResult;
import data.CellDAO;
import data.Datastore;

public class CellRDBMSDAO extends BaseDAO implements CellDAO, CellSQL, CellDAOConstants{
	private Datastore dataStore;
	
	public CellRDBMSDAO() {
		dataStore = JavaDBDatastore.getInstance();
	}

	@Override
	public void saveCells(int simulationId, int simulationResultId, SimulationResult cells) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		try {
			stmnt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			stmnt.setInt(1, simulationId);
			stmnt.setInt(2, simulationResultId);

			if (cells != null){
				for (int row = 0; row < cells.getRowCount(); row++) {
					for (int column = 0; column < cells.getColumnCount(); column++) {
						stmnt.setInt(3, row);
						stmnt.setInt(4, column);
						stmnt.setDouble(5, cells.getLongitude(row, column));
						stmnt.setDouble(6, cells.getLatitude(row, column));
						stmnt.setDouble(7, cells.getTemperature(row, column));
						
						stmnt.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmnt);
		}
	}
}