package data.impl;

public interface SimulationSQL {
	public static final String GET_BY_PK    =    "SELECT * FROM SIMULATION WHERE id=?";
	public static final String GET_BY_NAME  =    "SELECT * FROM SIMULATION WHERE name=?";

	public static final String DELETE_BY_PK =    "DELETE FROM SIMULATION WHERE id=?";
	public static final String INSERT       =    "INSERT INTO SIMULATION " + 
                                                 "(NAME, GRID_SPACING, TIME_STEP, LENGTH, ORBITAL_ECCENTRICITY, AXIAL_TILT) " + 
                                                 "VALUES (?,?,?,?,?,?)";
}
