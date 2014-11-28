package controllers;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import callbacks.OnCompleteListener;
import data.SimulationDAO;
import data.SimulationResultDAO;
import exceptions.ArgumentInvalidException;
import base.ObjectFactory;
import base.PersistenceManager;
import base.QueryBoundary;
import base.QueryMetrics;
import base.Simulation;
import base.SimulationMethod;
import base.SimulationParameters;
import base.SimulationResult;
import base.ThreadedProcess;
import base.Utils;
import base.Utils.InvocationParms;

public class SimulationController extends ThreadedProcess {
	private final static Logger LOGGER = Logger.getLogger(SimulationController.class.getName());
	
	private enum Mode{SIMULATION, QUERY};
	
	public final static double DEFAULT_AXIAL_TILT = 23.44;
	public final static double DEFAULT_ORBITAL_ECCENTRICITY = 0.0167;
	public final static int DEFAULT_GRID_SPACING = 15;
	public final static int DEFAULT_TIME_STEP = 1440;
	public final static int DEFAULT_LENGTH = 12;
	
	private double mAxialTilt = DEFAULT_AXIAL_TILT;
	private double mOrbitalEccentricity = DEFAULT_ORBITAL_ECCENTRICITY;
	private String mName = null;
	private Simulation simulation = null;
	private int mGridSpacing = DEFAULT_GRID_SPACING;
	private int mSimulationTimestep = DEFAULT_TIME_STEP;
	private int mSimulationLength = DEFAULT_LENGTH;
	
	private long startTime;
	private long endTime;
	private QueryBoundary queryBoundaries;

	private Mode mode;
	
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
	
	public SimulationResult simulate(SimulationResult previousResult, int sunPosition, int gridSpacing) throws InterruptedException {
		// TODO: Move hardcoded planetary values as high as possible
		return mSimulationMethod.simulate(previousResult, mAxialTilt, mOrbitalEccentricity, sunPosition, gridSpacing, 40030140.0, 0.3, 0.612, 149600000.0, 525600, 29555656845976000000.0);
	}
	
	public SimulationResult interpolate(SimulationResult previousResult, SimulationResult partialResult, int sunPosition, int gridSpacing) throws InterruptedException {
		// TODO: Move hardcoded planetary values as high as possible
		return mSimulationMethod.interpolate(previousResult, partialResult, mAxialTilt, mOrbitalEccentricity, sunPosition, gridSpacing, 40030140.0, 0.3, 0.612, 149600000.0, 525600, 29555656845976000000.0);
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
		mName = name;
		mGridSpacing = gridSpacing;
		mSimulationTimestep = simulationTimestep;
		mSimulationLength = simulationLength;
		
		this.mode = Mode.SIMULATION;
	}
	
	public void setQueryParameters(Simulation simulation, long startSim, long endSim, QueryBoundary boundaries) {
		this.simulation = simulation;
		this.startTime = startSim;
		this.endTime = endSim;
		
		this.mode = Mode.QUERY;
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
					boolean isNewSimulation = (mode == Mode.SIMULATION);
					if(mName != null){
						simulation = simulationDAO.getSimulationByName(mName);
					}
					if(simulation == null){
						isNewSimulation = true;
						simulation = createSimulation(mAxialTilt, mOrbitalEccentricity, mName, mGridSpacing, mSimulationTimestep, mSimulationLength);
					} else {
						//TODO check to see if stored simulation is long enough otherwise run new
						SimulationParameters params = simulation.getSimulationParameters();
						mAxialTilt = params.getAxialTilt();
						mOrbitalEccentricity = params.getOrbitalEccentricity();
						mName = simulation.getName();
						mGridSpacing = params.getGridSpacing();
						mSimulationTimestep = params.getTimeStep();
						mSimulationLength = params.getLength();
						if(!simulationLongEnough(simulation)){
							simulationDAO.removeSimulation(simulation.getId());
							simulation = createSimulation(mAxialTilt, mOrbitalEccentricity, mName, mGridSpacing, mSimulationTimestep, mSimulationLength);
							isNewSimulation = true;
						}
					}
					
					boolean reachedSimulationEnd = false;					
					final int sunIncrement = getDegreesFromTimestep();
					long minutesPassed = 0;
					int sunPosition = 0;
					if(!isNewSimulation){
						minutesPassed = startTime;
						sunPosition = calculateSunPositionFromTime(sunIncrement, minutesPassed);
					}
					
					SimulationResult previousResult = getInitalGrid();
					PersistenceManager manager = new PersistenceManager();
					QueryMetrics.getInstance().setFilter(queryBoundaries);

					// TODO: Check if we have reached the end of the simulation
					while (!checkStopped() && !reachedSimulationEnd) {
						checkPaused();
						
						SimulationResult newResult = null;
						if(isNewSimulation){
							newResult = simulate(previousResult, sunPosition, mGridSpacing);
						} else {
							long lo = minutesPassed - (mSimulationTimestep/2 - 1);
							long hi = minutesPassed + (mSimulationTimestep/2);
							SimulationResult dbResult = resultDAO.findSimulationResultBetween(simulation.getId(), lo, hi);
							if(dbResult == null){
								newResult = simulate(previousResult, sunPosition, mGridSpacing);
							} else {
								minutesPassed = dbResult.getSimulationTime();
								sunPosition = calculateSunPositionFromTime(sunIncrement, minutesPassed);
								newResult = interpolate(previousResult, dbResult, sunPosition, mGridSpacing);
							}
						}

						// TODO: Add stabilization check here
						
						if(isNewSimulation){
							newResult.setSunLatitude(calculateSunLat(mAxialTilt));
							newResult.setSunLongitude(calculateSunLong(sunPosition));
							newResult.setSimulationTime(minutesPassed);
	
							manager.saveResult(simulation, newResult);
						}
						
						mQueue.put(newResult);
						
						// Store result as previous result to input into next pass
						previousResult = newResult;
						
						// Increment the sun position and minutes passed based on the time step
						sunPosition += sunIncrement;
						minutesPassed += mSimulationTimestep;
						if(isNewSimulation){
							reachedSimulationEnd = convertMinutesToMonths(minutesPassed) >= mSimulationLength;
						} else {
							reachedSimulationEnd = minutesPassed >= endTime;
						}
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

			private int calculateSunPositionFromTime(int sunIncrement, long minutesPassed) {
				long steps = minutesPassed/mSimulationTimestep;
				
				return (int)steps * sunIncrement;
			}

		};
	}
	
	protected boolean simulationLongEnough(Simulation simulation) {
		return (convertMonthsToMinutes(simulation.getSimulationParameters().getLength()) >= endTime);
	}

	private SimulationResult getInitalGrid(){
		int rows = 180/mGridSpacing;
		int cols = 360/mGridSpacing;
		
		// Track previous result to use as start of next simulation
		// Initialize to initial grid as starting point
		return ObjectFactory.getInitialGrid(rows, cols);
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
	private double convertMinutesToMonths(long minutes) {
		// minutes / (1440 minutes in a day * 30 days in a Solar month)
		return minutes / 43200.0;
	}
	private long convertMonthsToMinutes(long months) {
		// minutes / (1440 minutes in a day * 30 days in a Solar month)
		return months * 43200;
	}

	protected static final double calculateSunLong(int sunPosition) {
		sunPosition = sunPosition%360;
		return (sunPosition<180)?(-sunPosition):(Math.abs(sunPosition-360));
	}
	
	protected static final double calculateSunLat(double axialTilt) {
		if(90>=axialTilt && axialTilt>=-90)
			return axialTilt;
		// TODO calculate inverted latitude
		return 0;
	}
	public static void main(String[] args) {
		for (int i = -100; i < 100; i++) {
			System.out.println(i + " : " + calculateSunLat(i));
		}
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
	
	private Simulation createSimulation(double axialTilt, double orbitalEccentricity, String name, int gridSpacing, int simulationTimestep, int simulationLength) {
		simulation = new Simulation();
		simulation.setName(name);

		SimulationParameters parameters = new SimulationParameters();
		parameters.setAxialTilt(axialTilt);
		parameters.setGridSpacing((short)gridSpacing);
		parameters.setLength((short)simulationLength);
		parameters.setOrbitalEccentricity(orbitalEccentricity);
		parameters.setTimeStep(simulationTimestep);

		InvocationParms invocationParms = (InvocationParms)System.getProperties().get(Utils.INVOCATION_PARAMETERS_KEY);
		if(invocationParms != null){
			parameters.setGeoPrecision(invocationParms.geographicPrecision);
			parameters.setPrecision(invocationParms.precision);
			parameters.setTempPrecision(invocationParms.temporalPrecision);
		}
		
		simulation.setSimulationParameters(parameters);
		int id = simulationDAO.saveSimulation(simulation);
		simulation.setId(id);
		
		return simulation;
	}
}
