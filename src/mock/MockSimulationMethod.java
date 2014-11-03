package mock;

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
	public SimulationResult simulate(SimulationResult previousResult) throws InterruptedException {
		int columnCount = previousResult.getColumnCount();
		int rowCount = previousResult.getRowCount();
		
		double[][] mockData = new double[rowCount][];
		for (int i = 0; i < mockData.length; i++) {
			double[] mockRow = new double[columnCount];
			for (int j = 0; j < mockRow.length; j++) {
				mockRow[j] = previousResult.getTemperature(i, j) + TEMPERATURE_INCREMENT + i * 5;
			}
			mockData[i] = mockRow;
		}
		
		Thread.sleep(500);
		
		return new SimulationResult(mockData);
	}

}
