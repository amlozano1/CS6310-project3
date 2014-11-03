package controllers;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import base.PresentationMethod;
import base.SimulationResult;
import base.ThreadedProcess;

public class PresentationController extends ThreadedProcess {

	private final static Logger LOGGER = Logger.getLogger(PresentationController.class.getName());
	
	/**
	 * The presentation method implementation to execute when the queue contains a simulation result.
	 */
	private final PresentationMethod mPresentationMethod;
	
	/**
	 * The shared data queue to add simulation result data to.
	 */
	private final BlockingQueue<SimulationResult> mQueue;
	
	public PresentationController(BlockingQueue<SimulationResult> queue, PresentationMethod presentationMethod) {
		mPresentationMethod = presentationMethod;
		mQueue = queue;
	}
	
	public void present(SimulationResult simulationResult) throws InterruptedException {
		mPresentationMethod.present(simulationResult);
	}

	@Override
	protected Runnable getRunnableAction() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					while (!checkStopped()) {
						checkPaused();
						
						SimulationResult result = mQueue.take();
						
						// TODO: Add presentation time step check
						present(result);
						
						log(Level.INFO, "Buffer size: " + mQueue.size() + "/" + (mQueue.size() + mQueue.remainingCapacity()));
					}
				} catch (InterruptedException e) {
					log(Level.INFO, "Presentation stopped by interrupt");
				}
			}

		};
	}
	
	@Override
	public String getThreadName() {
		return "Presentation";
	}

	@Override
	protected void log(Level level, String message) {
		LOGGER.log(level, message);
	}
	
}
