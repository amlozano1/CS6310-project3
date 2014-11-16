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
		double solarYear = 525600;
		double semiMajorAxis = 149600000;
		int time = 0;
		
		int columns = previousResult.getColumnCount();
		int rows = previousResult.getRowCount();
		double adjustedSolarPowerPerMeter = OrbitalPosition.getInverseSquare(orbitalEccentricity, time, solarYear, semiMajorAxis, solarPowerPerMeter);
		
		double averageTemp = previousResult.getAverageTemperature();
		
		// TODO: Change data type to our class
		double[][] data = new double[columns][];
		for (int column = 0; column < columns; column++) {
			data[column] = new double[rows];
			for (int row = 0; row < rows; row++) {
				double previous = previousResult.getTemperature(column, row);
				double previousNorth = row == 0 ? previous : previousResult.getTemperature(column, row - 1);
				double previousSouth = row + 1 == rows ? previous : previousResult.getTemperature(column, row + 1);
				double previousEast = column == 0 ? previous : previousResult.getTemperature(column - 1, row);
				double previousWest = column + 1 == columns ? previous : previousResult.getTemperature(column + 1, row);
				
				data[column][row] =
						CellCalculations.getSolarHeat(row + 1, column + 1, gridSpacing, time, circumference, adjustedSolarPowerPerMeter)
						+ CellCalculations.getCooling(row + 1, circumference, gridSpacing, previous, averageTemp, adjustedSolarPowerPerMeter)
						+ CellCalculations.getNeighborHeat(row + 1, circumference, gridSpacing, previousNorth, previousSouth, previousEast, previousWest);
			}
		}
		
		// TODO: Set additional simulation properties
		return new SimulationResult(data);
	}

}
