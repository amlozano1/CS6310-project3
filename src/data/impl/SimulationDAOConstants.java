package data.impl;

/**
 	NAME VARCHAR(50) NOT NULL,
	GRID_SPACING SMALLINT,
	TIME_STEP INTEGER,
	LENGTH SMALLINT,
	ORBITAL_ECCENTRICITY DOUBLE,
	AXIAL_TILT DOUBLE
 * @author ahigdon
 *
 */
public interface SimulationDAOConstants {
	public static final String TABLE_NAME = "simulation";
	
	public static final String NAME 		= "name";
	public static final String GRID_SPACING = "grid_spacing";
	public static final String TIME_STEP	= "time_step";
	public static final String LENGTH		= "length";
	public static final String ORBITAL_ECCENTRICITY = "ORBITAL_ECCENTRICITY";
	public static final String AXIAL_TILT 	= "AXIAL_TILT";
}
