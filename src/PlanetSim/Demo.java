package PlanetSim;

import controllers.MasterController;
import base.ObjectFactory;

/**
 * @author Tyler Benfield
 *
 */
public class Demo {
	
	/**
	 * Application entry point
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		MasterController controller = ObjectFactory.getMasterController();
		
		try {
			controller.start();
		
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
