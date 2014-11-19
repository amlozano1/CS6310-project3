package mock;

import base.PresentationMethod;
import base.SimulationResult;
import gui.*;

/**
 * Mock presentation method for testing purposes.
 * 
 * @author Tyler Benfield
 *
 */
public class MockPresentationMethod implements PresentationMethod {

	@Override
	public void present(SimulationResult result) throws InterruptedException {
		Thread.sleep(1000);
		System.out.println();
		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColumnCount(); j++) {
				EarthPanel.getInstance().updateGrid(result);
				//EarthPanel.getInstance().moveSunPosition(Buffer.getInstance().getHead().getfColumnUnderSun());
				//EarthPanel.getInstance().addTime(Buffer.getInstance().getHead().getElapsedTime());
			}
			System.out.println();
		}
	}

}
