package data.impl;

public interface SimulationSQL {
	public static final String GET_BY_PK = "SELECT * FROM SIMULATION WHERE name=?";
	public static final String INSERT = "INSERT INTO SIMULATION " + 
			"(NAME, GRID_SPACING, TIME_STEP, LENGTH, ORBITAL_ECCENTRICITY, AXIAL_TILT) " + 
			"VALUES (?,?,?,?,?,?)";
	
	public static final String DELETE = "DELETE FROM SIMULATION WHERE name=?";

}
