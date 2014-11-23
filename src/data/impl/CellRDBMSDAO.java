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
	public boolean saveCell(int simulationId, int simulationResultId, Cell cell, int row, int column) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		try {
			stmnt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			stmnt.setInt(1, simulationId);
			stmnt.setInt(2, simulationResultId);

			if (cell != null){
				return saveCell(cell, stmnt, row, column);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmnt);
		}
		
		return false;
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
						Cell cell = cells.getResultData()[row][column];
						saveCell(cell, stmnt, row, column);
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
	public boolean removeAllForSimulationResult(int simulationResultId) {
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
	
	@Override
	public boolean removeAllForSimulation(int simulationId) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		try {
			stmnt = conn.prepareStatement(DELETE_BY_SIMULATION_ID);
			stmnt.setInt(1, simulationId);
			int rows = stmnt.executeUpdate();
			return rows>0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmnt);
		}
		
		return false;
	}
	
	protected boolean saveCell(Cell cell, PreparedStatement stmnt, int row, int column) throws SQLException{
		stmnt.setInt(3, row);
		stmnt.setInt(4, column);
		stmnt.setDouble(5, cell.getLongitude());
		stmnt.setDouble(6, cell.getLatitude());
		stmnt.setDouble(7, cell.getTemperature());
		
		return stmnt.executeUpdate() > 0;
	}
}