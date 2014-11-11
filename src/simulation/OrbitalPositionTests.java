package simulation;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrbitalPositionTests {
	
	private static final double EARTH_SOLAR_YEAR = 525600;
	private static final double EARTH_ECCENTRICITY = 0.0167;
	private static final double EARTH_MAJOR_AXIS = 149600000.0;

	@Test
	public void meanAnomaly() {
		assertEquals(0, OrbitalPosition.getMeanAnomaly(0, EARTH_SOLAR_YEAR), 0.0);
		assertEquals(Math.PI, OrbitalPosition.getMeanAnomaly(262800, EARTH_SOLAR_YEAR), 0.0);
	}

	@Test
	public void eccentricAnomaly() {
		assertEquals(0, OrbitalPosition.getEccentricAnomaly(EARTH_ECCENTRICITY, 0, EARTH_SOLAR_YEAR), 0.0);
		assertEquals(Math.PI, OrbitalPosition.getEccentricAnomaly(EARTH_ECCENTRICITY, EARTH_SOLAR_YEAR / 2, EARTH_SOLAR_YEAR), 0.0);
	}

	@Test
	public void trueAnomaly() {
		assertEquals(0, OrbitalPosition.getTrueAnomaly(EARTH_ECCENTRICITY, 0, EARTH_SOLAR_YEAR), 0.0);
		assertEquals(Math.PI, OrbitalPosition.getTrueAnomaly(EARTH_ECCENTRICITY, EARTH_SOLAR_YEAR / 2, EARTH_SOLAR_YEAR), 0.0);
	}
	
	@Test
	public void distanceFromSun() {
		// TODO: Examine large difference
		assertEquals(147094000, OrbitalPosition.getDistanceFromSun(EARTH_ECCENTRICITY, 0, EARTH_SOLAR_YEAR, EARTH_MAJOR_AXIS), 10000);
		assertEquals(152093762, OrbitalPosition.getDistanceFromSun(EARTH_ECCENTRICITY, EARTH_SOLAR_YEAR / 2, EARTH_SOLAR_YEAR, EARTH_MAJOR_AXIS), 10000);
	}
	
	@Test
	public void minorAxis() {
		// Circle
		assertEquals(1, OrbitalPosition.getMinorAxis(0, 3, 4, 1), 0.0);
		
		// TODO: Examine large difference
		assertEquals(149579137.573318024529404935494783074902676516478, OrbitalPosition.getMinorAxis(EARTH_ECCENTRICITY, EARTH_SOLAR_YEAR / 2, EARTH_SOLAR_YEAR, EARTH_MAJOR_AXIS), 100000);
		assertEquals(149579137.573318024529404935494783074902676516478, OrbitalPosition.getMinorAxis(EARTH_ECCENTRICITY, EARTH_SOLAR_YEAR / 4, EARTH_SOLAR_YEAR, EARTH_MAJOR_AXIS), 100000);
		assertEquals(149579137.573318024529404935494783074902676516478, OrbitalPosition.getMinorAxis(EARTH_ECCENTRICITY, 3 * EARTH_SOLAR_YEAR / 4, EARTH_SOLAR_YEAR, EARTH_MAJOR_AXIS), 100000);
	}
	
	@Test
	public void coordinates() {
		double[] result = OrbitalPosition.getCoordinates(EARTH_ECCENTRICITY, 0, EARTH_SOLAR_YEAR, EARTH_MAJOR_AXIS);
		System.out.println(result[0] + ", " + result[1]);
		
		result = OrbitalPosition.getCoordinates(EARTH_ECCENTRICITY, EARTH_SOLAR_YEAR / 4, EARTH_SOLAR_YEAR, EARTH_MAJOR_AXIS);
		System.out.println(result[0] + ", " + result[1]);
		
		result = OrbitalPosition.getCoordinates(EARTH_ECCENTRICITY, EARTH_SOLAR_YEAR / 2, EARTH_SOLAR_YEAR, EARTH_MAJOR_AXIS);
		System.out.println(result[0] + ", " + result[1]);
		
		result = OrbitalPosition.getCoordinates(EARTH_ECCENTRICITY, 3 * EARTH_SOLAR_YEAR / 4, EARTH_SOLAR_YEAR, EARTH_MAJOR_AXIS);
		System.out.println(result[0] + ", " + result[1]);
		fail();
	}

}
