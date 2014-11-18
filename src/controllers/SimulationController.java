package controllers;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import callbacks.OnCompleteListener;
import data.SimulationDAO;
import data.SimulationResultDAO;
import exceptions.ArgumentInvalidException;
import base.ObjectFactory;
import base.Simulation;
import base.SimulationMethod;
import base.SimulationResult;
import base.ThreadedProcess;

public class SimulationController extends ThreadedProcess {
	
	private final static Logger LOGGER = Logger.getLogger(SimulationController.class.getName());
	
	public final static double DEFAULT_AXIAL_TILT = 23.44;
	public final static double DEFAULT_ORBITAL_ECCENTRICITY = 0.0167;
	public final static int DEFAULT_GRID_SPACING = 15;
	public final static int DEFAULT_TIME_STEP = 1440;
	public final static int DEFAULT_LENGTH = 12;
	
	private double mAxialTilt = DEFAULT_AXIAL_TILT;
	private double mOrbitalEccentricity = DEFAULT_ORBITAL_ECCENTRICITY;
	private Simulation simulation  = null;
	private int mGridSpacing = DEFAULT_GRID_SPACING;
	private int mSimulationTimestep = DEFAULT_TIME_STEP;
	private int mSimulationLength = DEFAULT_LENGTH;
	
	private SimulationDAO simulationDAO = ObjectFactory.getSimulationDAO();
	private SimulationResultDAO resultDAO = ObjectFactory.getSimulationResultDAO();
	
	private OnCompleteListener mOnCompleteListener;
	
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
	
	
	public void setOnCompleteListener(OnCompleteListener listener) {
		mOnCompleteListener = listener;
	}
	
	public SimulationResult simulate(SimulationResult previousResult, int sunPosition) throws InterruptedException {
		// TODO: May be able to remove axialTilt and orbitalEccentricity if they are contained in the previousResult
		return mSimulationMethod.simulate(previousResult, mAxialTilt, mOrbitalEccentricity, sunPosition);
	}
	
	/**
	 * Sets the simulation parameters to the specified values.
	 * 
	 * @param axialTilt
	 * @param orbitalEccentricity
	 * @param name
	 * @param gridSpacing
	 * @param simulationTimestep
	 * @param simulationLength
	 * @throws ArgumentInvalidException Thrown if any of the parameters have invalid values.
	 */
	public void setSimulationParameters(double axialTilt, double orbitalEccentricity, String name, int gridSpacing, int simulationTimestep, int simulationLength) throws ArgumentInvalidException {
		validateParameters(axialTilt, orbitalEccentricity, name, gridSpacing, simulationTimestep, simulationLength);
		mAxialTilt = axialTilt;
		mOrbitalEccentricity = orbitalEccentricity;
		mGridSpacing = gridSpacing;
		mSimulationTimestep = simulationTimestep;
		mSimulationLength = simulationLength;
		
		/*
		 * TODO this assumes simulation information (Physical Factors, Simulation Settings and Invocation Parameters) already persisted
		 * if this is not the case need to check if simulation exists, delete previous simulation, then save new simualtion information
		 * but must pass all into here
		 */
		simulation = simulationDAO.getSimulationByName(name);
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
					// Initialize to initial grid as starting point
					final int gridSize = getGridSize();
					SimulationResult previousResult = ObjectFactory.getInitialGrid(gridSize, gridSize);

					int minutesPassed = 0;
					boolean reachedSimulationEnd = false;
					
					// TODO: Decide if this should be a double or integer
					int sunPosition = 0;
					final int sunIncrement = getDegreesFromTimestep();
					
					// TODO: Ask database for matching simulation results to know what must be calculated and what is cached
					
					// TODO: Check if we have reached the end of the simulation
					while (!checkStopped() && !reachedSimulationEnd) {
						checkPaused();
						
						// TODO: Check if database had simulation result for this sun position

						SimulationResult newResult = simulate(previousResult, sunPosition);
						
						// TODO: Add stabilization check here
						
						// TODO: need to handle geo precision (is this result saved)
						// TODO: temporal precision (which cells are saved) is not handled in dao, this probably needs an abstraction layer to handle this 
						resultDAO.addSimulationResult(simulation.getId(), newResult);
						
						mQueue.put(newResult);
						
						// Store result as previous result to input into next pass
						previousResult = newResult;
						
						// Increment the sun position and minutes passed based on the time step
						sunPosition += sunIncrement;
						minutesPassed += mSimulationTimestep;
						reachedSimulationEnd = convertMinutesToMonths(minutesPassed) >= mSimulationLength;
					}
					
					if (reachedSimulationEnd) {
						LOGGER.info("Simulation reached completion");
						
						if (mOnCompleteListener != null) {
							mOnCompleteListener.complete();
						}
					} else {
						LOGGER.info("Simulation stopped by interrupt");
					}
				} catch (InterruptedException e) {
					LOGGER.info("Simulation stopped by interrupt");
				}
			}

		};
	}
	
	@Override
	protected String getThreadName() {
		return "Simulation";
	}

	@Override
	protected void log(Level level, String message) {
		LOGGER.log(level, message);
	}
	
	/**
	 * Converts the grid spacing from degrees to a grid size.
	 * 
	 * @return The number of cells the grid should have. Assumed to be square, so this value is the same for longitude and latitude.
	 */
	private int getGridSize() {
		return 360 / mGridSpacing;
	}
	
	/**
	 * Converts the simulation time step into the number of degrees passed in that time.
	 * 
	 * @return The number of degrees passed in the time step.
	 */
	private int getDegreesFromTimestep() {
		// TODO: Decide if this should be a double or integer return value
		return mSimulationTimestep / 4;
	}
	
	/**
	 * Converts a number of minutes into months.
	 * 
	 * @param minutes Minutes to convert
	 * @return The number of months
	 */
	private double convertMinutesToMonths(int minutes) {
		// minutes / (1440 minutes in a day * 30 days in a Solar month)
		return minutes / 43200.0;
	}

	private void validateParameters(double axialTilt, double orbitalEccentricity, String name, int gridSpacing, int simulationTimestep, int simulationLength) throws ArgumentInvalidException {
		if (axialTilt < -180 || axialTilt > 180) {
			throw new ArgumentInvalidException("axialTilt", "Axial tilt must be between -180 and 180 degrees");
		}
		if (orbitalEccentricity < 0 || orbitalEccentricity >= 1) {
			throw new ArgumentInvalidException("orbitalEccentricity", "Orbital eccentricity must be between 0 and 1");
		}
		if (name == null || name == "") {
			throw new ArgumentInvalidException("name", "Name cannot be empty");
		}
		if (gridSpacing < 1 || gridSpacing > 180 || 180 % gridSpacing  != 0) {
			throw new ArgumentInvalidException("gridSpacing", "Grid spacing must be between 1 and 180 degrees and evenly divide 180");
		}
		if (simulationTimestep < 1 || simulationTimestep > 525600) {
			throw new ArgumentInvalidException("simulationTimestep", "Simulation time step must be between 1 and 525,600 minutes");
		}
		if (simulationLength < 1 || simulationLength > 1200) {
			throw new ArgumentInvalidException("simulationLength", "Simulation length must be between 1 and 1200 Solar months");
		}
	}
	
}
