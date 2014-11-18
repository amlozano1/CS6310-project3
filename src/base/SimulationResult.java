package base;

import gui.TemperatureGrid;

/**
 * Contains result data from a single simulation execution.
 * 
 * @author Tyler Benfield
 *
 */
public class SimulationResult implements TemperatureGrid {
	
	// TODO: Add input parameters to this class
	// TODO: Make this class persistent
	
	// TODO: Change the type of this to our cell class
	private Cell[][] mResultData;
	private long simulationTime;
	private double sunLongitude;
	private double sunLatitude;

	public SimulationResult(Cell[][] resultData) {
		// TODO: Change the parameter type to our cell class
		mResultData = resultData;
	}
	
	/**
	 * Gets the number of columns in the result grid data
	 * 
	 * @return The number of columns in the result grid data
	 */
	public int getColumnCount() {
		return mResultData.length;
	}
	
	/**
	 * Gets the number of rows in the result grid data
	 * 
	 * @return The number of rows in the result grid data
	 */
	public int getRowCount() {
		return mResultData.length > 0 ? mResultData[0].length : 0;
	}

	/**
	 * Retrieves the height to the specified grid cell.
	 */
	public float getCellHeight(int x, int y) {
		// TODO: Fix this
		return 10;
	}

	/**
	 * Retrieves the temperature of the specified grid cell.
	 */
	public double getTemperature(int x, int y) {
		return mResultData[x][y] == null ? null : mResultData[x][y].getTemperature();
	}
	
	public double getLongitude(int x, int y) {
		return mResultData[x][y] == null ? null : mResultData[x][y].getLongitude();
	}
	
	public double getLatitude(int x, int y) {
		return mResultData[x][y] == null ? null : mResultData[x][y].getLatitude();
	}

	public long getSimulationTime() {
		return simulationTime;
	}

	public void setSimulationTime(long simulationTime) {
		this.simulationTime = simulationTime;
	}

	public double getSunLongitude() {
		return sunLongitude;
	}

	public void setSunLongitude(double sunLongitude) {
		this.sunLongitude = sunLongitude;
	}

	public double getSunLatitude() {
		return sunLatitude;
	}

	public void setSunLatitude(double sunLatitude) {
		this.sunLatitude = sunLatitude;
	}

	/**
	 * Gets the average temperature of the simulation data.
	 * 
	 * @return The average temperature of the simulation data
	 */
	public double getAverageTemperature() {
		double totalTemp = 0;
		int columns = mResultData.length;
		int rows = 1;
		for (int column = 0; column < columns; column++) {
			rows = mResultData[column].length;
			for (int row = 0; row < rows; row++) {
				totalTemp += mResultData[column][row].getTemperature();
			}
		}
		return totalTemp / (rows * columns);
	}
}
