package simulation;

import base.SimulationMethod;
import base.SimulationResult;

public class SimulationAlgorithm implements SimulationMethod {

	@Override
	public SimulationResult simulate(SimulationResult previousResult, double axialTilt, double orbitalEccentricity, int sunPosition) throws InterruptedException {
		
		// TODO: Decide where these belong as parameters
		double gridSpacing = 15;
		double circumference = 40030140.0;
		double solarPowerPerMeter = 1366;
		int time = 0;
		
		int columns = previousResult.getColumnCount();
		int rows = previousResult.getRowCount();
		
		// TODO: Change data type to our class
		double[][] data = new double[columns][];
		for (int column = 0; column < columns; column++) {
			data[column] = new double[rows];
			for (int row = 0; row < rows; row++) {
				double previous = previousResult.getTemperature(column, row);
				double previousNorth = row == 1 ? previous : previousResult.getTemperature(column, row - 1);
				double previousSouth = row + 1 == rows ? previous : previousResult.getTemperature(column, row + 1);
				double previousEast = column == 1 ? column : previousResult.getTemperature(column - 1, row);
				double previousWest = column + 1 == columns ? column : previousResult.getTemperature(column + 1, row);
				
				// TODO: Apply cooling
				data[column][row] = previous
						+ CellCalculations.getSolarHeat(row, column, gridSpacing, time, circumference, solarPowerPerMeter)
						+ CellCalculations.getNeighborHeat(row, circumference, gridSpacing, previousNorth, previousSouth, previousEast, previousWest);
			}
		}
		
		// TODO: Set additional simulation properties
		return new SimulationResult(data);
	}

}
