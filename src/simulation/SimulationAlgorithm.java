package simulation;

import java.util.logging.Logger;

import base.Cell;
import base.SimulationMethod;
import base.SimulationResult;

public class SimulationAlgorithm implements SimulationMethod {

	private final static Logger LOGGER = Logger.getLogger(SimulationAlgorithm.class.getName());

	@Override
	public SimulationResult simulate(SimulationResult previousResult, double axialTilt, double orbitalEccentricity, int sunPosition, int gridSpacing, double planetCircumference,
			double planetAldebo, double planetEmissivity, double orbitSemiMajorAxis, double solarYear, double solarPowerPerMeter) throws InterruptedException {
		
		int columns = previousResult.getColumnCount();
		int rows = previousResult.getRowCount();
		double adjustedSolarPowerPerMeter = OrbitalPosition.getInverseSquare(orbitalEccentricity, sunPosition, solarYear, orbitSemiMajorAxis, solarPowerPerMeter);
		LOGGER.info(String.format("Adjusted power per meter: %.5f", adjustedSolarPowerPerMeter));
		
		double[] coordinates = OrbitalPosition.getCoordinates( orbitalEccentricity,  sunPosition,  solarYear,  orbitSemiMajorAxis) ;
		LOGGER.info("Planet coordinates: " + Double.toString(coordinates[0]) + " " + Double.toString(coordinates[1]));
		
		double solarTemperatureAverage = CellCalculations.getKelvinFromSolarEnergy(adjustedSolarPowerPerMeter, planetAldebo, planetEmissivity);

		
		// TODO: Change data type to our class
		double cooling = 0;
		double heating = 0;
		Cell[][] data = new Cell[columns][];
		for (int column = 0; column < columns; column++) {
			data[column] = new Cell[rows];
			for (int row = 0; row < rows; row++) {
				double previous = previousResult.getTemperature(column, row);
				double previousNorth = row == 0 ? previous : previousResult.getTemperature(column, row - 1);
				double previousSouth = row + 1 == rows ? previous : previousResult.getTemperature(column, row + 1);
				double previousEast = column == 0 ? previous : previousResult.getTemperature(column - 1, row);
				double previousWest = column + 1 == columns ? previous : previousResult.getTemperature(column + 1, row);

				heating += CellCalculations.getSolarHeat(row, column, gridSpacing, sunPosition, planetCircumference, solarTemperatureAverage);
				cooling += CellCalculations.getCooling(row, planetCircumference, gridSpacing, solarTemperatureAverage);
				
				double temp = ((previous + heating + cooling) + previousNorth + previousSouth + previousEast + previousWest)/5;
				double longitude = 0;
				double latitude = 0;
				
				data[column][row] = new Cell(temp, longitude, latitude);
			}
		}
		LOGGER.info(String.format("Heating: %.5f", heating));
		LOGGER.info(String.format("Cooling: %.5f", cooling));
		
		// TODO: Set additional simulation properties
		return new SimulationResult(data);
	}

}
