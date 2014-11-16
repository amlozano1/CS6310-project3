package data.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import base.Simulation;
import base.SimulationParameters;
import data.Datastore;
import data.SimulationDAO;

public class SimulationRDBMSDAO extends BaseDAO implements SimulationDAO, SimulationSQL, SimulationDAOConstants{
	private Datastore dataStore;
	
	public SimulationRDBMSDAO() {
		dataStore = JavaDBDatastore.getInstance();
	}
	@Override
	public boolean saveSimulation(Simulation simulation) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		try {
			SimulationParameters parameters = simulation.getSimulationParameters();
			stmnt = conn.prepareStatement(INSERT);
			stmnt.setString(1, simulation.getName());
			stmnt.setShort(2, parameters.getGridSpacing());
			stmnt.setInt(3, parameters.getTimeStep());
			stmnt.setShort(4, parameters.getLength());
			stmnt.setDouble(5, parameters.getOrbitalEccentricity());
			stmnt.setDouble(6, parameters.getAxialTilt());
			stmnt.setShort(7, parameters.getPrecision());
			stmnt.setShort(8, parameters.getGeoPrecision());
			stmnt.setShort(9, parameters.getTempPrecision());

			int rows = stmnt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmnt);
		}
		
		return false;
	}
	
	@Override
	public boolean removeSimulation(Integer id) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		try {
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
	public Simulation getSimulation(Integer id) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		Simulation simulation = null;
		try {
			stmnt = conn.prepareStatement(GET_BY_PK);
			stmnt.setInt(1, id);
			rs = stmnt.executeQuery();
			if(rs.next())
				simulation = fromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, stmnt);
		}
		
		return simulation;
	}
	
	@Override
	public Simulation getSimulationByName(String name) {
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		Simulation simulation = null;
		try {
			stmnt = conn.prepareStatement(GET_BY_NAME);
			stmnt.setString(1, name);
			rs = stmnt.executeQuery();
			if(rs.next())
				simulation = fromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, stmnt);
		}
		
		return simulation;
	}
	
	private Simulation fromResultSet(ResultSet rs) {
		Simulation simulation = null;

		try {
			if(rs != null){
				simulation = new Simulation();
				SimulationParameters simulationParameters = new SimulationParameters();
				simulation.setId(rs.getInt(ID));
				simulation.setName(rs.getString(NAME));
				simulationParameters.setAxialTilt(rs.getDouble(AXIAL_TILT));
				simulationParameters.setGridSpacing(rs.getShort(GRID_SPACING));
				simulationParameters.setLength(rs.getShort(LENGTH));
				simulationParameters.setOrbitalEccentricity(rs.getDouble(ORBITAL_ECCENTRICITY));
				simulationParameters.setTimeStep(rs.getInt(TIME_STEP));
				simulationParameters.setPrecision(rs.getShort(PRECISION));
				simulationParameters.setGeoPrecision(rs.getShort(GEO_PRECISION));
				simulationParameters.setTempPrecision(rs.getShort(TEMPORAL_PRECISION));
				simulation.setSimulationParameters(simulationParameters);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return simulation;
	}
	
	@Override
	public List<Simulation> findSimulationBy(SimulationCriteria criteria) {
		List<Simulation> simulations = new ArrayList<Simulation>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLE_NAME);
		sql.append(" ").append(criteria.buildWhere());
		
		Connection conn = dataStore.getConnection();
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		Simulation simulation = null;
		try {
			stmnt = conn.prepareStatement(sql.toString());
			bind(stmnt, criteria);
			rs = stmnt.executeQuery();
			while(rs.next()){
				simulation = fromResultSet(rs);
				simulations.add(simulation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, stmnt);
		}
		
		return simulations;
	}
	
	private void bind(PreparedStatement stmnt, SimulationCriteria criteria) throws SQLException {
		List<Object> parameters = criteria.getParameters();
		int count = 0;
		for (Object object : parameters) {
			stmnt.setObject(++count, object);
		}
	}
}
