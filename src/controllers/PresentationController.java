package controllers;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import callbacks.OnCompleteListener;
import exceptions.ThreadException;
import base.PresentationMethod;
import base.QueryMetrics;
import base.SimulationResult;
import base.ThreadedProcess;

public class PresentationController extends ThreadedProcess {

	private final static Logger LOGGER = Logger.getLogger(PresentationController.class.getName());
	
	public final static int DEFAULT_DISPLAY_RATE = 1;
	
	private int mPresentationDisplayRate = DEFAULT_DISPLAY_RATE;
	private boolean mStopAndFlush = false;
	private OnCompleteListener mFlushCompleteListener;
	
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
	
	/**
	 * Sets the presentation parameters to the specified values. 
	 * 
	 * @param presentationDisplayRate
	 */
	public void setPresentationParameters(int presentationDisplayRate) {
		// TODO: Decide on display rate validation
		mPresentationDisplayRate = presentationDisplayRate;
	}
	
	public void present(SimulationResult simulationResult) throws InterruptedException {
		mPresentationMethod.present(simulationResult);
	}
	
	public void stopAndFlush(OnCompleteListener flushCompleteListener) throws ThreadException {
		mStopAndFlush = true;
		mFlushCompleteListener = flushCompleteListener;
	}

	@Override
	protected Runnable getRunnableAction() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					mStopAndFlush = false;
					mFlushCompleteListener = null;
					QueryMetrics metrics = QueryMetrics.getInstance();
					while (!checkStopped() && (!mStopAndFlush || mQueue.size() > 0)) {
						checkPaused();
						
						SimulationResult result = mQueue.take();
						metrics.addResult(result);
						
						// TODO: Add presentation display rate check
						present(result);
						
						log(Level.INFO, "Buffer size: " + mQueue.size() + "/" + (mQueue.size() + mQueue.remainingCapacity()));
					}
					
					// If presentation was flushed, call the flush complete listener
					if (mStopAndFlush && mFlushCompleteListener != null) {
						mFlushCompleteListener.complete();
					}
				} catch (InterruptedException e) {
					LOGGER.info("Presentation stopped by interrupt");
				}
			}

		};
	}
	
	@Override
	protected String getThreadName() {
		return "Presentation";
	}

	@Override
	protected void log(Level level, String message) {
		LOGGER.log(level, message);
	}
	
}
