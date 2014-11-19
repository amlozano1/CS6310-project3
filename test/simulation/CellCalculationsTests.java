package simulation;

import static org.junit.Assert.*;

import org.junit.Test;

public class CellCalculationsTests {
	
	private static final double EARTH_CIRCUMFERENCE = 40030140.0;
	private static final double EARTH_ALDEBO = 0.3;
	private static final double EARTH_EMISSIVITY = 0.612;
	private static final double SOLAR_ENERGY_FROM_SUN = 1366;

	@Test
	public void testGetCellHeight() {
		assertEquals(1667922.5, CellCalculations.getCellVerticalSideLength(EARTH_CIRCUMFERENCE, 15), 0.0);
	}
	
	@Test
	public void testGetNumberOfColumns() {
		assertEquals(24, CellCalculations.getNumberOfColumns(15), 0);
	}
	
	@Test
	public void testGetNumberOfRows() {
		assertEquals(12, CellCalculations.getNumberOfRows(15), 0);
	}
	
	@Test
	public void testGetLatitudeOfCellsInRow() {
		assertEquals(-75.0, CellCalculations.getLatitudeOfCellsInRow(1, 15), 0.0);
		assertEquals(-60.0, CellCalculations.getLatitudeOfCellsInRow(2, 15), 0.0);
		assertEquals(-45.0, CellCalculations.getLatitudeOfCellsInRow(3, 15), 0.0);
		assertEquals(-30.0, CellCalculations.getLatitudeOfCellsInRow(4, 15), 0.0);
		assertEquals(-15.0, CellCalculations.getLatitudeOfCellsInRow(5, 15), 0.0);
		assertEquals(0.0, CellCalculations.getLatitudeOfCellsInRow(6, 15), 0.0);
		assertEquals(15.0, CellCalculations.getLatitudeOfCellsInRow(7, 15), 0.0);
		assertEquals(30.0, CellCalculations.getLatitudeOfCellsInRow(8, 15), 0.0);
		assertEquals(45.0, CellCalculations.getLatitudeOfCellsInRow(9, 15), 0.0);
		assertEquals(60.0, CellCalculations.getLatitudeOfCellsInRow(10, 15), 0.0);
		assertEquals(75.0, CellCalculations.getLatitudeOfCellsInRow(11, 15), 0.0);
		assertEquals(90.0, CellCalculations.getLatitudeOfCellsInRow(12, 15), 0.0);
	}
	
	@Test
	public void testGetBaseWidth() {
		assertEquals(431690.0, CellCalculations.getBaseWidth(1, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(833961.25, CellCalculations.getBaseWidth(2, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1179399.25, CellCalculations.getBaseWidth(3, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1444463.25, CellCalculations.getBaseWidth(4, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1611089.5, CellCalculations.getBaseWidth(5, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getBaseWidth(6, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1611089.5, CellCalculations.getBaseWidth(7, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1444463.25, CellCalculations.getBaseWidth(8, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1179399.25, CellCalculations.getBaseWidth(9, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(833961.25, CellCalculations.getBaseWidth(10, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(431690.0, CellCalculations.getBaseWidth(11, EARTH_CIRCUMFERENCE, 15), 1.0);
	}
	
	@Test
	public void testGetCeilingWidth() {
		// TODO: Check these calculations, 0.0 seems weird for the last one
		assertEquals(833961.25, CellCalculations.getCeilingWidth(1, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1179399.25, CellCalculations.getCeilingWidth(2, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1444463.25, CellCalculations.getCeilingWidth(3, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1611089.5, CellCalculations.getCeilingWidth(4, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getCeilingWidth(5, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1611089.5, CellCalculations.getCeilingWidth(6, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1444463.25, CellCalculations.getCeilingWidth(7, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1179399.25, CellCalculations.getCeilingWidth(8, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(833961.25, CellCalculations.getCeilingWidth(9, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(431690.0, CellCalculations.getCeilingWidth(10, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(0.0, CellCalculations.getCeilingWidth(11, EARTH_CIRCUMFERENCE, 15), 1.0);
	}
	
	@Test
	public void testGetAltitude() {
		assertEquals(1667922.5, CellCalculations.getAltitude(1, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getAltitude(2, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getAltitude(3, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getAltitude(4, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getAltitude(5, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getAltitude(6, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getAltitude(7, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getAltitude(8, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getAltitude(9, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getAltitude(10, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1667922.5, CellCalculations.getAltitude(11, EARTH_CIRCUMFERENCE, 15), 1.0);
	}
	
	@Test
	public void testGetPerimeter() {
		// TODO: Check these calculations
		assertEquals(4601496.25, CellCalculations.getPerimeter(1, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(5349205.5, CellCalculations.getPerimeter(2, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(5959707.5, CellCalculations.getPerimeter(3, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(6391397.5, CellCalculations.getPerimeter(4, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(6614857.0, CellCalculations.getPerimeter(5, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(6614857.0, CellCalculations.getPerimeter(6, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(6391397.5, CellCalculations.getPerimeter(7, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(5959707.5, CellCalculations.getPerimeter(8, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(5349205.5, CellCalculations.getPerimeter(9, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(4601496.25, CellCalculations.getPerimeter(10, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(3767535.0, CellCalculations.getPerimeter(11, EARTH_CIRCUMFERENCE, 15), 1.0);
	}
	
	@Test
	public void testGetArea() {
		// TODO: Check these calculations
		assertEquals(1055504189211.526000, CellCalculations.getArea(1, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1679064689521.469200, CellCalculations.getArea(2, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(2188199706026.120000, CellCalculations.getArea(3, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(2548212528736.083500, CellCalculations.getArea(4, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(2734568878732.995000, CellCalculations.getArea(5, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(2734568878732.995000, CellCalculations.getArea(6, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(2548212528736.083500, CellCalculations.getArea(7, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(2188199706026.120000, CellCalculations.getArea(8, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1679064689521.469200, CellCalculations.getArea(9, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(1055504189211.526000, CellCalculations.getArea(10, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(360012822709.963440, CellCalculations.getArea(11, EARTH_CIRCUMFERENCE, 15), 1.0);
	}
	
	@Test
	public void testGetDistanceFromPrimeMeridian() {
		// TODO: Check these calculations
		assertEquals(1667922.5, CellCalculations.getDistanceFromPrimeMeridian(0, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(3335845.0, CellCalculations.getDistanceFromPrimeMeridian(1, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(5003767.5, CellCalculations.getDistanceFromPrimeMeridian(2, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(6671690.0, CellCalculations.getDistanceFromPrimeMeridian(3, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(8339612.5, CellCalculations.getDistanceFromPrimeMeridian(4, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(10007535.0, CellCalculations.getDistanceFromPrimeMeridian(5, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(11675457.5, CellCalculations.getDistanceFromPrimeMeridian(6, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(13343380.0, CellCalculations.getDistanceFromPrimeMeridian(7, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(15011302.5, CellCalculations.getDistanceFromPrimeMeridian(8, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(16679225.0, CellCalculations.getDistanceFromPrimeMeridian(9, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(18347147.5, CellCalculations.getDistanceFromPrimeMeridian(10, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(20015070.0, CellCalculations.getDistanceFromPrimeMeridian(11, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-18347147.5, CellCalculations.getDistanceFromPrimeMeridian(12, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-16679225.0, CellCalculations.getDistanceFromPrimeMeridian(13, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-15011302.5, CellCalculations.getDistanceFromPrimeMeridian(14, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-13343380.0, CellCalculations.getDistanceFromPrimeMeridian(15, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-11675457.5, CellCalculations.getDistanceFromPrimeMeridian(16, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-10007535.0, CellCalculations.getDistanceFromPrimeMeridian(17, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-8339612.5, CellCalculations.getDistanceFromPrimeMeridian(18, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-6671690.0, CellCalculations.getDistanceFromPrimeMeridian(19, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-5003767.5, CellCalculations.getDistanceFromPrimeMeridian(20, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-3335845.0, CellCalculations.getDistanceFromPrimeMeridian(21, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-1667922.5, CellCalculations.getDistanceFromPrimeMeridian(22, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(0.0, CellCalculations.getDistanceFromPrimeMeridian(23, EARTH_CIRCUMFERENCE, 15), 1.0);
	}
	
	@Test
	public void testGetDistanceFromEquator() {
		// TODO: Check these calculations
		assertEquals(-20015070.0, CellCalculations.getDistanceFromEquator(0, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-16679225.0, CellCalculations.getDistanceFromEquator(1, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-13343380.0, CellCalculations.getDistanceFromEquator(2, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-10007535.0, CellCalculations.getDistanceFromEquator(3, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-6671690.0, CellCalculations.getDistanceFromEquator(4, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(-3335845.0, CellCalculations.getDistanceFromEquator(5, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(0.0, CellCalculations.getDistanceFromEquator(6, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(3335845.0, CellCalculations.getDistanceFromEquator(7, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(6671690.0, CellCalculations.getDistanceFromEquator(8, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(10007535.0, CellCalculations.getDistanceFromEquator(9, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(13343380.0, CellCalculations.getDistanceFromEquator(10, EARTH_CIRCUMFERENCE, 15), 1.0);
		assertEquals(16679225.0, CellCalculations.getDistanceFromEquator(11, EARTH_CIRCUMFERENCE, 15), 1.0);
	}
	
	@Test
	public void testGetRadiansFromDegrees() {
		assertEquals(0.0, CellCalculations.getRadiansFromDegrees(0), 0.0);
		assertEquals(Math.PI, CellCalculations.getRadiansFromDegrees(180), 0.0);
		assertEquals(2.0 * Math.PI, CellCalculations.getRadiansFromDegrees(360), 0.0);
	}
	
	@Test
	public void testGetRotationalAngle() {
		assertEquals(0.0, CellCalculations.getRotationalAngle(0), 0.0);
		assertEquals(90.0, CellCalculations.getRotationalAngle(360), 0.0);
		assertEquals(180.0, CellCalculations.getRotationalAngle(720), 0.0);
		assertEquals(270.0, CellCalculations.getRotationalAngle(1080), 0.0);
		assertEquals(0.0, CellCalculations.getRotationalAngle(1440), 0.0);
	}
	
	@Test
	public void testGetHeatAttenuation() {
		// TODO: Determine test cases for this
		System.out.println(CellCalculations.getNumberOfColumns(180));
		System.out.println(CellCalculations.getNumberOfRows(180));
		System.out.println(CellCalculations.getHeatAttenuation(1, 1, 180, 720));
	}
	
	@Test
	public void testGetBorderEastWest() {
		// TODO: Determine test cases for this
		//assertEquals(0.0, CellCalculations.getBorderEastWest(1, EARTH_CIRCUMFERENCE, 15), 0.0);
	}
	
	@Test
	public void testGetBorderNorth() {
		// TODO: Determine test cases for this
		//assertEquals(0.0, CellCalculations.getBorderNorth(1, EARTH_CIRCUMFERENCE, 15), 0.0);
	}
	
	@Test
	public void testGetBorderSouth() {
		// TODO: Determine test cases for this
		//assertEquals(0.0, CellCalculations.getBorderSouth(1, EARTH_CIRCUMFERENCE, 15), 0.0);
	}
	
	@Test
	public void testGetNeighborHeat() {
		assertEquals(288.0, CellCalculations.getNeighborHeat(1, EARTH_CIRCUMFERENCE, 15, 288, 288, 288, 288), 0);
	}
	
	@Test
	public void testGetSolarHeat() {
		// TODO: Determine test cases for this
	}
	
	@Test
	public void testGetCooling() {
		// TODO: Determine test cases for this
	}
	
	@Test
	public void testGetKelvinFromSolarEnergy() {
		assertEquals(288.0, CellCalculations.getKelvinFromSolarEnergy(SOLAR_ENERGY_FROM_SUN, EARTH_ALDEBO, EARTH_EMISSIVITY), 1.0);
	}

}
