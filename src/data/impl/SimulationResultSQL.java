package data.impl;

public interface SimulationResultSQL {
	public static final String INSERT = "INSERT INTO SIMULATION_RESULT "
			                          + "(SIMULATION_ID,SIMULATION_STEP_ID,GRIDX,GRIDY,LONGITUDE,LATITUDE,TEMPERATURE) "
			                          + "VALUES (?,?,?,?,?,?,?);";
}
