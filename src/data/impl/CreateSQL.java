package data.impl;

public final class CreateSQL {
	public static final String CREATE_SIMULATION = "CREATE TABLE APP.SIMULATION "
                                                 + "("
                                                 + "ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                                                 + "NAME VARCHAR(50) NOT NULL, "
                                                 + "GRID_SPACING SMALLINT, "
                                                 + "TIME_STEP INTEGER, "
                                                 + "LENGTH SMALLINT, "
                                                 + "ORBITAL_ECCENTRICITY FLOAT, "
                                                 + "AXIAL_TILT FLOAT, "
                                                 + "PRECISION SMALLINT, "
                                                 + "GEO_PRECISION SMALLINT, "
                                                 + "TEMPORAL_PRECISION SMALLINT"
                                                 + ")";
	
	public static final String CREATE_SIMULATION_RESULT = "CREATE TABLE APP.SIMULATION_RESULT "
														+ "("
														+ "ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
														+ "SIMULATION_ID INTEGER NOT NULL,"
														+ "SIMULATION_TIME BIGINT NOT NULL, "
														+ "ROW_COUNT INTEGER NOT NULL,"
														+ "COLUMN_COUNT INTEGER NOT NULL, "
														+ "OCCURENCE_DATE DATE NOT NULL,"
														+ "OCCURENCE_TIME TIME NOT NULL,"
														+ "SUN_LONGITUDE FLOAT NOT NULL, "
														+ "SUN_LATITUDE FLOAT NOT NULL "
														+ ")";
	
	public static final String CREATE_CELL  = "CREATE TABLE APP.CELL "
											+ "("
											+ "ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
											+ "SIMULATION_ID INTEGER NOT NULL, "
											+ "SIMULATION_RESULT_ID INTEGER NOT NULL, "
											+ "GRIDX SMALLINT NOT NULL, "
											+ "GRIDY SMALLINT NOT NULL, "
											+ "LONGITUDE FLOAT NOT NULL, "
											+ "LATITUDE FLOAT NOT NULL, "
											+ "TEMPERATURE FLOAT NOT NULL "
											+ ")";
	
	public static final String CREATE_SIMULATION_RESULT_FK = "ALTER TABLE APP.SIMULATION_RESULT "
			                                               + "ADD FOREIGN KEY (SIMULATION_ID) "
			                                               + "REFERENCES APP.SIMULATION(ID)";
	
	public static final String CREATE_CELL_FK = "ALTER TABLE APP.CELL "
                                              + "ADD FOREIGN KEY (SIMULATION_ID) "
                                              + "REFERENCES APP.SIMULATION(ID) ";

	public static final String CREATE_CELL_FK2 = "ALTER TABLE APP.CELL "
                                               + "ADD FOREIGN KEY (SIMULATION_RESULT_ID) "
                                               + "REFERENCES APP.SIMULATION_RESULT(ID)";
	
	
	public static String[] CREATE_SQL = { 
		 CREATE_SIMULATION, 
		 CREATE_SIMULATION_RESULT, 
		 CREATE_SIMULATION_RESULT_FK, 
		 CREATE_CELL, 
		 CREATE_CELL_FK, 
		 CREATE_CELL_FK2};
	
}
