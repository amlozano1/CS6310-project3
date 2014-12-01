package base;

/**
 * PresentationMethod is an interface to define a module that would present
 * simulation information to the user. The implementation is only responsible
 * for presenting a single SimulationResult. Threading, synchronization, and
 * other factors are handled in caller.
 * 
 * @author Tyler Benfield
 *
 */
public interface PresentationMethod {
	
	public void present(SimulationResult result) throws InterruptedException;

}
