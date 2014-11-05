package PlanetSim;

import java.util.logging.Logger;

import controllers.MasterController;
import controllers.PresentationController;
import exceptions.ArgumentInvalidException;
import base.ObjectFactory;
import base.Utils;

/**
 * @author Tyler Benfield
 *
 */
public class Demo {

	private final static Logger LOGGER = Logger.getLogger(Demo.class.getName());
	
	/**
	 * Application entry point
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		Utils.InvocationParms invocationParms;
		try {
			invocationParms = Utils.parseArguments(args);
		} catch (ArgumentInvalidException ex) {
			System.err.println("Invalid argument: " + ex.getMessage());
			return;
		}
		
		MasterController controller = ObjectFactory.getMasterController();
		
		try {
			controller.start();
		
			Thread.sleep(2000);
		
			controller.pause();
		
			Thread.sleep(2000);
			
			controller.resume();
			
			Thread.sleep(5000);
			
			controller.stop();
			
			System.out.println("Complete");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
