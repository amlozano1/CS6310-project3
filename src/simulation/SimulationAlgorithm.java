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
		
		double cooling = 0;
		double heating = 0;
		Cell[][] data = new Cell[columns][];
		for (int column = 0; column < columns; column++) {
			data[column] = new Cell[rows];
			double longitude = CellCalculations.getLongitudeOfCellsInColumn(column, gridSpacing);
			
			for (int row = 0; row < rows; row++) {
				double previous = previousResult.getTemperature(column, row);
				double previousNorth = row == 0 ? previous : previousResult.getTemperature(column, row - 1);
				double previousSouth = row + 1 == rows ? previous : previousResult.getTemperature(column, row + 1);
				double previousEast = column == 0 ? previous : previousResult.getTemperature(column - 1, row);
				double previousWest = column + 1 == columns ? previous : previousResult.getTemperature(column + 1, row);

				heating += CellCalculations.getSolarHeat(row, column, gridSpacing, sunPosition, planetCircumference, solarTemperatureAverage);
				cooling += CellCalculations.getCooling(row, planetCircumference, gridSpacing, solarTemperatureAverage);
				
				double temp = ((previous + heating + cooling) + previousNorth + previousSouth + previousEast + previousWest)/5;
				double latitude = CellCalculations.getLatitudeOfCellsInRow(row, gridSpacing);;
				
				data[column][row] = new Cell(temp, longitude, latitude);
			}
		}
		
		// Note: these should balance out
		LOGGER.info(String.format("Heating: %.5f", heating));
		LOGGER.info(String.format("Cooling: %.5f", cooling));
		
		return new SimulationResult(data);
	}

	@Override
	public SimulationResult interpolate(SimulationResult partialResult, double axialTilt, double orbitalEccentricity, int sunPosition, int gridSpacing,
			double planetCircumference, double planetAldebo, double planetEmissivity, double orbitSemiMajorAxis, double solarYear, double solarPowerPerMeter) throws InterruptedException {
		
		int columns = partialResult.getColumnCount();
		int rows = partialResult.getRowCount();
		double adjustedSolarPowerPerMeter = OrbitalPosition.getInverseSquare(orbitalEccentricity, sunPosition, solarYear, orbitSemiMajorAxis, solarPowerPerMeter);
		LOGGER.info(String.format("Adjusted power per meter: %.5f", adjustedSolarPowerPerMeter));
		
		double[] coordinates = OrbitalPosition.getCoordinates( orbitalEccentricity,  sunPosition,  solarYear,  orbitSemiMajorAxis) ;
		LOGGER.info("Planet coordinates: " + Double.toString(coordinates[0]) + " " + Double.toString(coordinates[1]));
		
		for (int column = 0; column < columns; column++) {
			double longitude = CellCalculations.getLongitudeOfCellsInColumn(column, gridSpacing);
			
			for (int row = 0; row < rows; row++) {
				Cell value = partialResult.getCell(column, row);
				
				if (value == null) {
					// Find the closest cell to each border and average the temperatures
					// TODO: Weigh the average by the distance from the current cell and the border ratio, similar to neighbor heating
					
					double latitude = CellCalculations.getLatitudeOfCellsInRow(row, gridSpacing);;
					double temp = 0;
					int counter = 0;
					
					Cell north = null;
					for (int i = row - 1; i >= 0 && north == null; i--) {
						north = partialResult.getCell(column, i);
					}
					if (north != null) {
						temp += north.getTemperature();
						counter++;
					}
					
					Cell south = null;
					for (int i = row + 1; i < rows && south == null; i++) {
						south = partialResult.getCell(column, i);
					}
					if (south != null) {
						temp += south.getTemperature();
						counter++;
					}
					
					Cell east = null;
					for (int i = column + 1; i < columns && east == null; i++) {
						east = partialResult.getCell(i, row);
					}
					if (east != null) {
						temp += east.getTemperature();
						counter++;
					}
					
					Cell west = null;
					for (int i = column - 1; i >= 0 && west == null; i--) {
						west = partialResult.getCell(i, row);
					} 
					if (west != null) {
						temp += west.getTemperature();
						counter++;
					}
					
					if (counter > 0) {
						temp = temp / counter;
					}
					
					partialResult.setCell(column, row, new Cell(temp, longitude, latitude));
				}
			}
		}
		
		return partialResult;
	}

}
