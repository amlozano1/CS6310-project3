package base;

import Visualizer.TemperatureGrid;

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
	private double[][] mResultData;
	
	public SimulationResult(double[][] resultData) {
		// TODO: Change the parameter type to our cell class
		mResultData = resultData;
	}
	
	/**
	 * Gets the number of columns in the result grid data
	 * 
	 * @return The number of columns in the result grid data
	 */
	public int getColumnCount() {
		return mResultData.length > 0 ? mResultData[0].length : 0;
	}
	
	/**
	 * Gets the number of rows in the result grid data
	 * 
	 * @return The number of rows in the result grid data
	 */
	public int getRowCount() {
		return mResultData.length;
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
		return mResultData[x][y];
	}
	
}
