package data.impl;

public interface CellSQL {

	public static final String INSERT = "INSERT INTO CELL " + 
                                        "(SIMULATION_ID,SIMULATION_RESULT_ID,GRIDX,GRIDY,LONGITUDE,LATITUDE,TEMPERATURE) " + 
                                        "VALUES (?,?,?,?,?,?,?)";
	
	public static final String GET_GRID_SIZE = "SELECT MAX(GRIDX) as x, MAX(GRIDY) as y FROM CELL WHERE SIMULATION_RESULT_ID = ?";
	public static final String GET_BY_SIMULATION_RESULT_ID = "SELECT * FROM CELL WHERE SIMULATION_RESULT_ID = ?";
	
	public static final String DELETE_BY_SIMULATION_RESULT_ID = "DELETE FROM CELL WHERE SIMULATION_RESULT_ID = ?";
}
