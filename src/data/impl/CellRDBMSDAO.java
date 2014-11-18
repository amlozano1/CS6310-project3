package data.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import base.Cell;
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
	
	@Override
	public Cell[][] getCellsForSimulationResult(int simulationResultId) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmntForSize, stmnt = null;
		ResultSet rs = null;
		try {
			Cell[][] data = null;
			stmntForSize = conn.prepareStatement(GET_GRID_SIZE);
			stmntForSize.setInt(1, simulationResultId);
			rs = stmntForSize.executeQuery();
			if(rs.next()){
				int columns = rs.getInt("x") + 1;
				int rows = rs.getInt("y") + 1;
				data = new Cell[rows][columns];
				for (int x = 0; x < data.length; x++) {
					for (int y = 0; y < data[x].length; y++) {
						data[x][y] = null;
					}
				}
				close(stmntForSize);
				close(rs);
				
				stmnt = conn.prepareStatement(GET_BY_SIMULATION_RESULT_ID);
				stmnt.setInt(1, simulationResultId);
				rs = stmnt.executeQuery();

				while(rs.next()){
					Cell cell = new Cell();
					cell.setTemperature(rs.getDouble(TEMPERATURE));
					cell.setLatitude(rs.getDouble(LATITUDE));
					cell.setLongitude(rs.getDouble(LONGITUDE));
					int x = rs.getInt(GRIDX);
					int y = rs.getInt(GRIDY);

					data[x][y] = cell;
				}
			}
			
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmnt);
		}

		return null;
	}
	
	@Override
	public boolean removeCellsForSimulationResult(int simulationResultId) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		try {
			stmnt = conn.prepareStatement(DELETE_BY_SIMULATION_RESULT_ID);
			stmnt.setInt(1, simulationResultId);
			int rows = stmnt.executeUpdate();
			return rows>0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmnt);
		}
		
		return false;
	}
}