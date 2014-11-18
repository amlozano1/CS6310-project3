package data.impl;

public interface SimulationResultSQL {
	public static final String GET_BY_PK         = "SELECT * FROM SIMULATION_RESULT WHERE ID = ?";
	public static final String GET_BY_SIMULATION = "SELECT * FROM SIMULATION_RESULT WHERE SIMULATION_ID = ? ORDER BY SIMULATION_TIME";

	public static final String INSERT = "INSERT INTO SIMULATION_RESULT "
			                          + "(SIMULATION_ID,SIMULATION_TIME,OCCURENCE_DATE,OCCURENCE_TIME,SUN_LONGITUDE,SUN_LATITUDE) "
			                          + "VALUES (?,?,?,?,?,?)";
	
	public static final String DELETE_BY_PK            = "DELETE FROM SIMULATION_RESULT WHERE ID = ?";
	public static final String DELETE_BY_SIMULATION_ID = "DELETE FROM SIMULATION_RESULT WHERE SIMULATION_ID = ?";

}
