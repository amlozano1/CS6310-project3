package data.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import base.Cell;
import base.ObjectFactory;
import base.SimulationResult;
import base.Utils;
import data.Datastore;
import data.SimulationResultDAO;

public class SimulationResultRDBMSDAO extends BaseDAO implements SimulationResultDAO, SimulationResultSQL, SimulationResultDAOConstants {
	private static final Calendar TIME_ZONE = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

	private Datastore dataStore;

	public SimulationResultRDBMSDAO() {
		dataStore = JavaDBDatastore.getInstance();
	}

	@Override
	public SimulationResult getSimulationResult(int id) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		SimulationResult simulationResult = null;
		try {
			stmnt = conn.prepareStatement(GET_BY_PK);
			stmnt.setInt(1, id);
			rs = stmnt.executeQuery();
			if(rs.next()){
				simulationResult = fromResultSet(rs);
				Cell[][] data = ObjectFactory.getCellDAO().getCellsForSimulationResult(id);
				simulationResult.setResultData(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmnt);
		}
		
		return simulationResult;
	}
	
	@Override
	public SimulationResult findSimulationResult(int simulationId, long simulationTime) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		SimulationResult simulationResult = null;
		try {
			stmnt = conn.prepareStatement(GET_BY_SIMULATION_TIME);
			stmnt.setInt(1, simulationId);
			stmnt.setLong(2, simulationTime);
			rs = stmnt.executeQuery();
			if(rs.next()){
				simulationResult = fromResultSet(rs);
				Cell[][] data = ObjectFactory.getCellDAO().getCellsForSimulationResult(simulationResult.getId());
				simulationResult.setResultData(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(stmnt);
		}
		
		return simulationResult;
	}
	
	@Override
	public Integer addSimulationResult(int simulationId, SimulationResult simulationResult) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		try {
			stmnt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			stmnt.setInt(1, simulationId);
			stmnt.setLong(2, simulationResult.getSimulationTime());
			stmnt.setDate(3, Utils.toDate(simulationResult.getSimulationTime()), TIME_ZONE);
			stmnt.setTime(4, Utils.toTime(simulationResult.getSimulationTime()), TIME_ZONE);
			stmnt.setDouble(5, simulationResult.getSunLongitude());
			stmnt.setDouble(6, simulationResult.getSunLatitude());
			
			stmnt.executeUpdate();
			
			ResultSet rs = stmnt.getGeneratedKeys();
			Integer simulationResultId = null;
			if(rs.next()){
				simulationResultId = rs.getInt(1);
			}
			
//			CellDAO cellDAO = ObjectFactory.getCellDAO();
//			cellDAO.saveCells(simulationId, simulationResultId, simulationResult);
			
			return simulationResultId;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmnt);
		}
		
		return null;
	}

	@Override
	public boolean removeSimulationResult(int id) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		try {
			ObjectFactory.getCellDAO().removeAllForSimulationResult(id);
			stmnt = conn.prepareStatement(DELETE_BY_PK);
			stmnt.setInt(1, id);
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
	public List<SimulationResult> getAllForSimulation(int simulationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAllForSimulation(int simulationId) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		try {
			ObjectFactory.getCellDAO().removeAllForSimulation(simulationId);
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

	@Override
	public List<SimulationResult> findForTimeRange(int simulationId, long simulationTimeStart, long simulationTimeStop) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private SimulationResult fromResultSet(ResultSet rs) {
		SimulationResult result = null;
		try {
			if(rs != null){
				result = new SimulationResult();
				result.setId(rs.getInt(ID));
				result.setSimulationTime(rs.getLong(SIMULATION_TIME));
				result.setSunLatitude(rs.getDouble(SUN_LATITUDE));
				result.setSunLongitude(rs.getDouble(SUN_LONGITUDE));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
