package PlanetSim;

import java.util.logging.Logger;

import controllers.MasterController;
import exceptions.ArgumentInvalidException;
import gui.UI;
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
		try {
			// This will put the arguments into a globally accessible location
			Utils.parseArguments(args);
		} catch (ArgumentInvalidException ex) {
			System.err.println("Invalid argument: " + ex.getMessage());
			return;
		}
		
		MasterController controller = ObjectFactory.getMasterController();
		
		UI gui = UI.getInstance();
		gui.setController(controller);
		LOGGER.info("GUI created");
	}

}
