package simulation;

import static org.junit.Assert.*;

import org.junit.Test;

public class TiltTests {
	
	private static final double EARTH_SOLAR_YEAR = 525600;
	private static final double EARTH_OBLIQUITY = 23.44;
	private static final double EARTH_ARGUMENT_OF_PERIAPSIS = 116640;

	@Test
	public void sunAngle() {
		// March 21, from assignment description
		assertEquals(0, Tilt.getSunAngle(EARTH_ARGUMENT_OF_PERIAPSIS, EARTH_SOLAR_YEAR, EARTH_ARGUMENT_OF_PERIAPSIS, EARTH_OBLIQUITY), 0.0); 
		
		// June 21, from assignment description
		assertEquals(23.44, Tilt.getSunAngle(247680, EARTH_SOLAR_YEAR, EARTH_ARGUMENT_OF_PERIAPSIS, EARTH_OBLIQUITY), 0.001);
	}

}
