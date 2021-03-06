package base;

import gui.PresentationLayer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import simulation.SimulationAlgorithm;
import controllers.MasterController;
import data.SimulationDAOFactory;
import data.SimulationDAO;
import data.SimulationDAOFactory.DaoType;
import data.SimulationResultDAO;
import data.CellDAO;

/**
 * ObjectFactory provides methods of creating implementations of interface
 * objects.
 * 
 * This helps decouple the various pieces of the architecture by having a single
 * location responsible for managing implementations.
 * 
 * @author Tyler Benfield
 *
 */
public final class ObjectFactory {
	
	private static final int BUFFER_CAPACITY = 10;
	
	private static final double INITIAL_GRID_TEMPERATURE = 288;

	/**
	 * Private constructor is used to prevent the default constructor from being public.
	 * This makes the entire class static.
	 */
	private ObjectFactory() { }
	
	public static SimulationResult getInitialGrid(int rowCount, int columnCount) {
		
		Cell[][] initialData = new Cell[columnCount][];
		for (int i = 0; i < initialData.length; i++) {
			Cell[] initialRow = new Cell[rowCount];
			for (int j = 0; j < initialRow.length; j++) {
				initialRow[j] = new Cell(INITIAL_GRID_TEMPERATURE, 0d, 0d);
			}
			initialData[i] = initialRow;
		}
		
		return new SimulationResult(initialData);
		
	}
	
	public static MasterController getMasterController() {
		return new MasterController(getBlockingQueue(BUFFER_CAPACITY), getPresentationMethod(), getSimulationMethod());
	}
	
	public static SimulationDAO getSimulationDAO(){
		return getSimualtionDAOFactory().getSimulationDAO();
	}
	
	public static CellDAO getCellDAO(){
		return getSimualtionDAOFactory().getCellDAO();
	}
	
	public static SimulationResultDAO getSimulationResultDAO(){
		return getSimualtionDAOFactory().getSimulationResultDAO();
	}
	
	private static BlockingQueue<SimulationResult> getBlockingQueue(int capacity) {
		return new ArrayBlockingQueue<SimulationResult>(capacity);
	}
	
	private static PresentationMethod getPresentationMethod() {
		return new PresentationLayer();
	}
	
	private static SimulationMethod getSimulationMethod() {
		return new SimulationAlgorithm();
	}
	
	private static SimulationDAOFactory getSimualtionDAOFactory(){
		return SimulationDAOFactory.getSimualtionDAOFactory(DaoType.RDBMS);
	}
}
