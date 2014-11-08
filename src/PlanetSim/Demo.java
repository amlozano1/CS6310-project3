package PlanetSim;

import java.util.logging.Logger;

import callbacks.OnCompleteListener;
import controllers.MasterController;
import controllers.PresentationController;
import controllers.SimulationController;
import exceptions.ArgumentInvalidException;
import base.ObjectFactory;
import base.Utils;

/**
 * @author Tyler Benfield
 *
 */
public class Demo {

	private final static Logger LOGGER = Logger.getLogger(Demo.class.getName());
	
	private final static int PRESENTATION_DISPLAY_RATE = PresentationController.DEFAULT_DISPLAY_RATE;
	private final static double SIMULATION_AXIAL_TILT = SimulationController.DEFAULT_AXIAL_TILT;
	private final static double SIMULATION_ORBITAL_ECCENTRICITY = SimulationController.DEFAULT_ORBITAL_ECCENTRICITY;
	private final static String SIMULATION_NAME = "Simulation";
	private final static int SIMULATION_GRID_SPACING = SimulationController.DEFAULT_GRID_SPACING;
	private final static int SIMULATION_TIME_STEP = SimulationController.DEFAULT_TIME_STEP;
	private final static int SIMULATION_LENGTH = SimulationController.DEFAULT_LENGTH;
	
	/**
	 * Application entry point
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		Utils.InvocationParms invocationParms;
		try {
			invocationParms = Utils.parseArguments(args);
			// TODO: Do something with invocation parms, this will likely be used by the DAO
		} catch (ArgumentInvalidException ex) {
			System.err.println("Invalid argument: " + ex.getMessage());
			return;
		}
		
		MasterController controller = ObjectFactory.getMasterController();
		controller.setOnCompleteListener(new OnCompleteListener() {
			
			@Override
			public void complete() {
				System.out.println("Complete");
			}
			
		});
		
		// TODO: Add the GUI here
		// TODO: The below code is an example of user behavior and should be removed with the GUI is implemented
		
		try {
			controller.start(SIMULATION_AXIAL_TILT, SIMULATION_ORBITAL_ECCENTRICITY, SIMULATION_NAME, SIMULATION_GRID_SPACING, SIMULATION_TIME_STEP, SIMULATION_LENGTH, PRESENTATION_DISPLAY_RATE);
		
			Thread.sleep(2000);
		
			controller.pause();
		
			Thread.sleep(2000);
			
			controller.resume();
			
			Thread.sleep(5000);
			
			controller.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
