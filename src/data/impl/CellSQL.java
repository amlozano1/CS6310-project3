package data.impl;

public interface CellSQL {

	public static final String INSERT = "INSERT INTO SIMULATION " + 
                                        "(SIMULATION_ID,SIMULATION_RESULT_ID,GRIDX,GRIDY,LONGITUDE,LATITUDE,TEMPERATURE) " + 
                                        "VALUES (?,?,?,?,?,?,?)";}
