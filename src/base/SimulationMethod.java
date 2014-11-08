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
	
	// TODO: May be able to remove axialTilt and orbitalEccentricity if they are contained in the previousResult
	public SimulationResult simulate(SimulationResult previousResult, double axialTilt, double orbitalEccentricity, int sunPosition) throws InterruptedException;

}
