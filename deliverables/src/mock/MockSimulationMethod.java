package mock;

import base.Cell;
import base.SimulationMethod;
import base.SimulationResult;

/**
 * Mock simulation method for testing purposes.
 * 
 * @author Tyler Benfield
 *
 */
public class MockSimulationMethod implements SimulationMethod {
	
	private static final double TEMPERATURE_INCREMENT = 5;

	@Override
	public SimulationResult simulate(SimulationResult previousResult,
			double axialTilt, double orbitalEccentricity, int sunPosition,
			int gridSpacing, double planetCircumference, double planetAldebo,
			double planetEmissivity, double orbitSemiMajorAxis,
			double solarYear, double solarPowerPerMeter)
			throws InterruptedException {
		int columnCount = previousResult.getColumnCount();
		int rowCount = previousResult.getRowCount();
		
		Cell[][] mockData = new Cell[rowCount][];
		for (int i = 0; i < mockData.length; i++) {
			Cell[] mockRow = new Cell[columnCount];
			for (int j = 0; j < mockRow.length; j++) {
				double temp = (previousResult.getTemperature(i, j) + TEMPERATURE_INCREMENT + i * 5) + j;
				Cell c = new Cell(temp, 0d, 0d);
				mockRow[j] = c;
			}
			mockData[i] = mockRow;
		}
		
		Thread.sleep(500);
		
		return new SimulationResult(mockData);
	}

	@Override
	public SimulationResult interpolate(SimulationResult partialResult, double axialTilt,
			double orbitalEccentricity, int sunPosition, int gridSpacing,
			double planetCircumference, double planetAldebo,
			double planetEmissivity, double orbitSemiMajorAxis,
			double solarYear, double solarPowerPerMeter)
			throws InterruptedException {
		int columnCount = partialResult.getColumnCount();
		int rowCount = partialResult.getRowCount();
		
		Cell[][] mockData = new Cell[rowCount][];
		for (int i = 0; i < mockData.length; i++) {
			Cell[] mockRow = new Cell[columnCount];
			for (int j = 0; j < mockRow.length; j++) {
				double temp = (288 + TEMPERATURE_INCREMENT + i * 5) + j;
				Cell c = new Cell(temp, 0d, 0d);
				mockRow[j] = c;
			}
			mockData[i] = mockRow;
		}
		
		Thread.sleep(500);
		
		return new SimulationResult(mockData);
	}

}
