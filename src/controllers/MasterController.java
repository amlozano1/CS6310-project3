package controllers;

import java.util.concurrent.BlockingQueue;

import base.PresentationMethod;
import base.SimulationMethod;
import base.SimulationResult;

public class MasterController {

	private final PresentationController mPresenationController;
	private final SimulationController mSimulationController;
	
	public MasterController(BlockingQueue<SimulationResult> queue, PresentationMethod presentationMethod, SimulationMethod simulationMethod) {
		mPresenationController = new PresentationController(queue, presentationMethod);
		mSimulationController = new SimulationController(queue, simulationMethod);
	}
	
	public void pause() throws Exception {
		// TODO: Make custom exception type
		mPresenationController.pause();
		mSimulationController.pause();
	}
	
	public void resume() throws Exception {
		// TODO: Make custom exception type
		mPresenationController.resume();
		mSimulationController.resume();
	}
	
	public void start() throws Exception {
		// TODO: Make custom exception type
		// TODO: Add input parameters here
		mPresenationController.start();
		mSimulationController.start();
	}
	
	public void stop() throws Exception {
		// TODO: Make custom exception type
		mPresenationController.stop();
		mSimulationController.stop();
	}

}
