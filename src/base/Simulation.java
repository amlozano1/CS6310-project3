package base;

/**
 * @author ahigdon
 *
 */
public class Simulation {
	private Integer id;
	private String name;
	private SimulationParameters simulationParameters;
		
	public Simulation(Integer id, String name, SimulationParameters parameters) {
		super();
		this.id = id;
		this.name = name;
		this.simulationParameters = parameters;
	}

	public Simulation(Integer id, String name) {
		this(id, name, (SimulationParameters)null);
	}
	
	public Simulation() {
		this((Integer)null, (String)null, (SimulationParameters)null);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SimulationParameters getSimulationParameters() {
		return simulationParameters;
	}
	public void setSimulationParameters(SimulationParameters parameters) {
		this.simulationParameters = parameters;
	}
}
