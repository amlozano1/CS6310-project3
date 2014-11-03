package controllers;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import base.ObjectFactory;
import base.SimulationMethod;
import base.SimulationResult;
import base.ThreadedProcess;

public class SimulationController extends ThreadedProcess {
	
	private final static Logger LOGGER = Logger.getLogger(SimulationController.class.getName());
	
	/**
	 * The shared data queue to add simulation result data to.
	 */
	private final BlockingQueue<SimulationResult> mQueue;

	/**
	 * The simulation method implementation to execute on each simulation attempt.
	 */
	private final SimulationMethod mSimulationMethod;
	
	public SimulationController(BlockingQueue<SimulationResult> queue, SimulationMethod simulationMethod) {
		mQueue = queue;
		mSimulationMethod = simulationMethod;
	}
	
	public SimulationResult simulate(SimulationResult previousResult) throws InterruptedException {
		return mSimulationMethod.simulate(previousResult);
	}
	
	/**
	 * Returns the task to run inside of the thread. Used by the super class.
	 */
	@Override
	protected Runnable getRunnableAction() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					// Track previous result to use as start of next simulation
					// TODO: Don't use hardcoded value here
					SimulationResult previousResult = ObjectFactory.getInitialGrid(20, 20);
					
					while (!checkStopped()) {
						checkPaused();

						SimulationResult newResult = simulate(previousResult);
						
						// TODO: Add stabilization check here

						mQueue.put(newResult);
						previousResult = newResult;
					}
				} catch (InterruptedException e) {
					log(Level.INFO, "Simulation stopped by interrupt");
				}
			}

		};
	}
	
	@Override
	public String getThreadName() {
		return "Simulation";
	}

	@Override
	protected void log(Level level, String message) {
		LOGGER.log(level, message);
	}

}
