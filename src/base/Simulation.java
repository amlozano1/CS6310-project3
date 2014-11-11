package base;

/**
 * @author ahigdon
 *
 */
public class Simulation {
	private String name;
	private SimulationParameters simulationParameters;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SimulationParameters getSimulationParameters() {
		return simulationParameters;
	}
	public void setSimulationParameters(SimulationParameters simulationParameters) {
		this.simulationParameters = simulationParameters;
	}
}
