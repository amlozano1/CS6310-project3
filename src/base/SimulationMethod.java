/**
 * 
 */
package base;

/**
 * SimulationMethod is an interface to define a module that would execute a
 * single simulation run. The implementation is only responsible for executing a
 * single simulation and generating result data. Threading, synchronization, and
 * other factors are handled in caller.
 * 
 * @author Tyler Benfield
 *
 */
public interface SimulationMethod {
	
	public SimulationResult simulate(SimulationResult previousResult) throws InterruptedException;

}
