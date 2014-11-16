package base;

/**
 * Contains result data from a single simulation execution.
 * 
 * @author Tyler Benfield
 *
 */
public class SimulationResult {
	
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
		return mResultData[x][y];
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
				totalTemp += mResultData[column][row];
			}
		}
		return totalTemp / (rows * columns);
	}
	
}
