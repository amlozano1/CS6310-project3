package controllers;

import java.util.concurrent.BlockingQueue;

import callbacks.OnCompleteListener;
import exceptions.ArgumentInvalidException;
import exceptions.ThreadException;
import gui.UI;
import base.PresentationMethod;
import base.QueryBoundary;
import base.QueryMetrics;
import base.Simulation;
import base.SimulationMethod;
import base.SimulationParameters;
import base.SimulationResult;

public class MasterController {

	private final PresentationController mPresenationController;
	private final SimulationController mSimulationController;
	private final OnSimulationCompleteListener mOnSimulationCompleteListener;
	
	private OnCompleteListener mOnCompleteListener;
	private boolean runPresentation = false;
	
	public MasterController(BlockingQueue<SimulationResult> queue, PresentationMethod presentationMethod, SimulationMethod simulationMethod) {
		mPresenationController = new PresentationController(queue, presentationMethod);
		mSimulationController = new SimulationController(queue, simulationMethod);
		mOnSimulationCompleteListener = new OnSimulationCompleteListener();
	}
	
	public void setPresentationControllerDisplayRate(int displayRate){
		mPresenationController.setPresentationParameters(displayRate);
	}
	
	public void pause() throws ThreadException {
		mSimulationController.pause();
		if(this.runPresentation)
			mPresenationController.pause();
	}
	
	public void resume() throws ThreadException {
		mSimulationController.resume();
		if(this.runPresentation) 
			mPresenationController.resume();
	}

	/**
	 * Sets the listener to call when a simulation reaches completion or is stopped.
	 * 
	 * @param listener The listener to call when a simulation reaches completion or is stopped.
	 */
	public void setOnCompleteListener(OnCompleteListener listener) {
		mOnCompleteListener = listener;
	}
	
	public void start(double axialTilt, double orbitalEccentricity, String name, int gridSpacing, int simulationTimestep, int startTime, int simulationLength, int presentationDisplayRate, boolean startPresentation) throws ArgumentInvalidException, ThreadException {
		mSimulationController.setSimulationParameters(axialTilt, orbitalEccentricity, name, gridSpacing, simulationTimestep, simulationLength);
		start(startPresentation);
	}
	
	public void query( Simulation simulation, long startSim, long endSim, QueryBoundary boundaries, boolean startPresentation) throws ArgumentInvalidException, ThreadException{
		mSimulationController.setQueryParameters(simulation, startSim, endSim, boundaries);
		start(startPresentation);
	}
	
	private void start(boolean startPresentation) throws ThreadException{
		mSimulationController.setOnCompleteListener(mOnSimulationCompleteListener);
		mSimulationController.start();
		this.runPresentation = startPresentation;
		if(this.runPresentation) 
			mPresenationController.start();	
	}
	public void stop() throws ThreadException {
		mSimulationController.stop();
		if(this.runPresentation) 
			mPresenationController.stop();
		
		// If the complete listener is set, call it
		if (mOnCompleteListener != null) {
			mOnCompleteListener.complete();
		}
	}
	
	/**
	 * Implementation of OnCompleteListener for simulation completion.
	 * Initialized once in constructor to eliminate the need to declare an anonymous listener each time start is called.
	 * Not static so that it can access MasterController instance references.
	 */
	private class OnSimulationCompleteListener implements OnCompleteListener {

		@Override
		public void complete() {
			try {
				// Stop and flush the presentation so that any results that have not been displayed will not be lost
				// Pass the complete lister in so that any outside actions will be called when the flush is finished
				UI.getInstance().completeSimulation();
				UI.getInstance().updateMetricResults();
				if(runPresentation)
					mPresenationController.stopAndFlush(mOnCompleteListener);
			} catch (ThreadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				// If the complete listener is set, call it
				if (mOnCompleteListener != null) {
					mOnCompleteListener.complete();
				}
			}
		}
		
	}
}
