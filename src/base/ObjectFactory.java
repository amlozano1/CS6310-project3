package base;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import mock.MockPresentationMethod;
import mock.MockSimulationMethod;
import controllers.MasterController;

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
	
	private static final double INITIAL_GRID_TEMPERATURE = 10;

	/**
	 * Private constructor is used to prevent the default constructor from being public.
	 * This makes the entire class static.
	 */
	private ObjectFactory() { }
	
	public static SimulationResult getInitialGrid(int rowCount, int columnCount) {
		
		double[][] initialData = new double[rowCount][];
		for (int i = 0; i < initialData.length; i++) {
			double[] initialRow = new double[columnCount];
			for (int j = 0; j < initialRow.length; j++) {
				initialRow[j] = INITIAL_GRID_TEMPERATURE;
			}
			initialData[i] = initialRow;
		}
		
		return new SimulationResult(initialData);
		
	}
	
	public static MasterController getMasterController() {
		return new MasterController(getBlockingQueue(BUFFER_CAPACITY), getPresentationMethod(), getSimulationMethod());
	}
	
	private static BlockingQueue<SimulationResult> getBlockingQueue(int capacity) {
		return new ArrayBlockingQueue<SimulationResult>(capacity);
	}
	
	private static PresentationMethod getPresentationMethod() {
		return new MockPresentationMethod();
	}
	
	private static SimulationMethod getSimulationMethod() {
		return new MockSimulationMethod();
	}
	
}
