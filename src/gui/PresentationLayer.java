package gui;

import base.PresentationMethod;
import base.SimulationResult;

public class PresentationLayer implements PresentationMethod {
	@Override
	public void present(SimulationResult result) throws InterruptedException {
		// TODO Auto-generated method stub
		//Thread.sleep(1000);
		//System.out.println();
		EarthPanel.getInstance().updateGrid(result);
		TimeDisplayQuery.getInstance().setTime(result.getSimulationTime());
		TimeDisplaySimulation.getInstance().setTime(result.getSimulationTime());
	}

}
