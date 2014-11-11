package simulation;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrbitalPositionTests {
	
	private static final double EARTH_ECCENTRICITY = 0.0167;
	private static final double EARTH_MAJOR_AXIS = 149600000.0;

	@Test
	public void meanAnomaly() {
		assertEquals(0, OrbitalPosition.getMeanAnomaly(0, 525600), 0.0);
		assertEquals(Math.PI, OrbitalPosition.getMeanAnomaly(262800, 525600), 0.0);
	}

	@Test
	public void eccentricAnomaly() {
		assertEquals(0, OrbitalPosition.getEccentricAnomaly(EARTH_ECCENTRICITY, 0, 525600), 0.0);
		assertEquals(Math.PI, OrbitalPosition.getEccentricAnomaly(EARTH_ECCENTRICITY, 262800, 525600), 0.0);
	}

	@Test
	public void trueAnomaly() {
		assertEquals(0, OrbitalPosition.getTrueAnomaly(EARTH_ECCENTRICITY, 0, 525600), 0.0);
		assertEquals(Math.PI, OrbitalPosition.getTrueAnomaly(EARTH_ECCENTRICITY, 262800, 525600), 0.0);
	}
	
	@Test
	public void distanceFromSun() {
		assertEquals(147094000, OrbitalPosition.getDistanceFromSun(EARTH_ECCENTRICITY, 0, 525600, EARTH_MAJOR_AXIS), 10000);
		assertEquals(152093762, OrbitalPosition.getDistanceFromSun(EARTH_ECCENTRICITY, 262800, 525600, EARTH_MAJOR_AXIS), 10000);
	}

}
