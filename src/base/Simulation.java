package base;

public class Simulation implements SimulationMethod{
	private static final double TEMPERATURE_INCREMENT = 5;

	@Override
	public SimulationResult simulate(SimulationResult previousResult, double axialTilt, double orbitalEccentricity, int sunPosition) throws InterruptedException {
		int columnCount = previousResult.getColumnCount();
		int rowCount = previousResult.getRowCount();
		
		double[][] newPlanet = new double[rowCount][];
		for (int i = 0; i < newPlanet.length; i++) {
			double[] currentRow = new double[columnCount];
			for (int j = 0; j < currentRow.length; j++) {
				currentRow[j] = previousResult.getTemperature(i, j) + TEMPERATURE_INCREMENT + i * 5;
			}
			newPlanet[i] = currentRow;
		}
		
		Thread.sleep(500);
		
		return new SimulationResult(newPlanet);
	}
}
