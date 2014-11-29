package simulation;

import static org.junit.Assert.*;

import org.apache.derby.iapi.error.PassThroughException;
import org.junit.Test;

import simulation.CellCalculations;
import simulation.SimulationAlgorithm;
import base.ObjectFactory;
import base.SimulationResult;

public class SimulationAlgorithmTests {

	private static final double EARTH_SOLAR_YEAR = 525600;
	private static final double EARTH_ECCENTRICITY = 0.0167;
	private static final double EARTH_MAJOR_AXIS = 149600000.0;
	private static final double EARTH_OBLIQUITY = 0;// 23.44;
	private static final double EARTH_ARGUMENT_OF_PERIAPSIS = 116640;
	private static final double EARTH_CIRCUMFERENCE = 40030140.0;
	private static final double EARTH_SOLAR_POWER_PER_METER = 29555656845976000000.0;
	private static final double EARTH_ALDEBO = 0.3;
	private static final double EARTH_EMISSIVITY = 0.612;


	@Test
	public void test() {
		SimulationAlgorithm alg = new SimulationAlgorithm();
		try {
			SimulationResult result = ObjectFactory.getInitialGrid(CellCalculations.getNumberOfRows(15), CellCalculations.getNumberOfColumns(15));
			int sunPosition = 0;
			for (int i = 0; i < 20; i++) {
				result = alg.simulate(result, EARTH_OBLIQUITY, EARTH_ECCENTRICITY, sunPosition, 15, EARTH_CIRCUMFERENCE, EARTH_ALDEBO, EARTH_EMISSIVITY, EARTH_MAJOR_AXIS, EARTH_SOLAR_YEAR, EARTH_SOLAR_POWER_PER_METER);
				sunPosition = (sunPosition + 15 ) % 360;
			}

			double area = 0;
			for (int row = 0; row < result.getRowCount(); row++) {
				for (int column = 0; column < result.getColumnCount(); column++) {
					area += CellCalculations.getArea(row, EARTH_CIRCUMFERENCE, 15);
					System.out.print(result.getTemperature(column, row) + "\t");
				}
				System.out.println();
			}
			System.out.println();
			
			System.out.println(String.format("Area: %.6f", area));
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInterpolate() {
		SimulationAlgorithm alg = new SimulationAlgorithm();
		try {
			SimulationResult result = ObjectFactory.getInitialGrid(CellCalculations.getNumberOfRows(15), CellCalculations.getNumberOfColumns(15));
			int rows = result.getRowCount();
			int columns = result.getColumnCount();
			
			for (int column = 0; column < columns; column++) {
				if (column % 2 == 1) {
					for (int row = 0; row < rows; row++) {
						if (row % 2 == 1) {
							result.setCell(column, row, null);
							assertEquals(null, result.getCell(column, row));
						}
					}
				}
			}
			
			result = alg.interpolate(result, EARTH_OBLIQUITY, EARTH_ECCENTRICITY, 0, 15, EARTH_CIRCUMFERENCE, EARTH_ALDEBO, EARTH_EMISSIVITY, EARTH_MAJOR_AXIS, EARTH_SOLAR_YEAR, EARTH_SOLAR_POWER_PER_METER);

			for (int row = 0; row < result.getRowCount(); row++) {
				for (int column = 0; column < result.getColumnCount(); column++) {
					assertEquals(288, result.getTemperature(column, row), 0.0);
				}
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInterpolate2() {
		SimulationAlgorithm alg = new SimulationAlgorithm();
		try {
			SimulationResult result = ObjectFactory.getInitialGrid(CellCalculations.getNumberOfRows(15), CellCalculations.getNumberOfColumns(15));
			int rows = result.getRowCount();
			int columns = result.getColumnCount();
			
			for (int column = 0; column < columns; column++) {
				if (column % 3 == 1) {
					for (int row = 0; row < rows; row++) {
						if (row % 3 == 1) {
							result.setCell(column, row, null);
							assertEquals(null, result.getCell(column, row));
						}
					}
				}
			}
			
			result = alg.interpolate(result, EARTH_OBLIQUITY, EARTH_ECCENTRICITY, 0, 15, EARTH_CIRCUMFERENCE, EARTH_ALDEBO, EARTH_EMISSIVITY, EARTH_MAJOR_AXIS, EARTH_SOLAR_YEAR, EARTH_SOLAR_POWER_PER_METER);

			for (int row = 0; row < result.getRowCount(); row++) {
				for (int column = 0; column < result.getColumnCount(); column++) {
					assertEquals(288, result.getTemperature(column, row), 0.0);
				}
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
