package base;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.QueryBoundaryException;

public class QueryMetricsTest {
	private QueryMetrics metrics;
	
	@Before
	public void setUp() throws Exception {
		metrics = QueryMetrics.getInstance();
		metrics.clear();
		for (int i = 0; i < 10; i++) {
			SimulationResult result = new SimulationResult(createCellData());
			result.setSimulationTime(i * 10);
			metrics.addResult(result);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetMeanForRegion() {
		int cells = 25; //5x5 grid
		int sum = (24 * ( 24 + 1))/2;//sum of 1,2,3...n = n(n+1)/2
		double expectedAvg = (sum * 100)/cells;
		
		for (int time = 0; time < 5; time++) {
			double temp = metrics.getMeanForRegion(0l);
			assertEquals("Time [" + time * 10 + "] failed", expectedAvg, temp, 0);
		}
	}

	@Test
	public void testGetMeanForTime() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				double temp = metrics.getMeanForTime((i*5) + j);
				double expectedAvg = ((i*5) + j) * 100;
				assertEquals("Region [" + i + " x " + j +"] failed", expectedAvg, temp, 0);
			}
		}
	}

	@Test
	public void testGetMax() {
		Cell max = metrics.getMax();
		int expectedCellRow = 4;
		int expectedCellColumn = 4;
		double expectedLat = -90d + (36 * expectedCellRow);
		double expectedLong = -180d + (72 * expectedCellColumn);
		double expectedTemp = ((expectedCellRow * 5 ) + expectedCellColumn) * 100;
		
		assertNotNull(max);
		assertEquals(expectedLong, max.getLongitude().doubleValue(), 0);
		assertEquals(expectedLat, max.getLatitude().doubleValue(), 0);
		assertEquals(expectedTemp, max.getTemperature().doubleValue(), 0);
	}

	@Test
	public void testGetMin() {
		Cell min = metrics.getMin();
		int expectedCellRow = 0;
		int expectedCellColumn = 0;
		double expectedLat = -90d + (36 * expectedCellRow);
		double expectedLong = -180d + (72 * expectedCellColumn);
		double expectedTemp = ((expectedCellRow * 5 ) + expectedCellColumn) * 100;
		
		assertNotNull(min);
		assertEquals(expectedLong, min.getLongitude().doubleValue(), 0);
		assertEquals(expectedLat, min.getLatitude().doubleValue(), 0);
		assertEquals(expectedTemp, min.getTemperature().doubleValue(), 0);
	}

	@Test
	public void testGetMinTime() {
		Cell[][] data = createCellData();
		data[data.length-1][data.length-1] = new Cell(-Double.MAX_VALUE, 180d, 90d);
		SimulationResult result = new SimulationResult(data);
		result.setSimulationTime(Long.MAX_VALUE);
		
		metrics.addResult(result);
		assertEquals(Long.MAX_VALUE, metrics.getMinTime());
	}

	@Test
	public void testGetMaxTime() {
		Cell[][] data = createCellData();
		data[data.length-1][data.length-1] = new Cell(Double.MAX_VALUE, 180d, 90d);
		SimulationResult result = new SimulationResult(data);
		result.setSimulationTime(Long.MAX_VALUE);
		
		metrics.addResult(result);
		assertEquals(Long.MAX_VALUE, metrics.getMaxTime());
	}
	
	@Test
	public void testFilter() {
		try {
			metrics.clear();
			
			Double north = 20d;
			Double south = 0d;
			Double east = 40d;
			Double west = 0d;

			QueryBoundary filter = new QueryBoundary(north, south, east, west);
			metrics.setFilter(filter);

			for (int i = 0; i < 10; i++) {
				SimulationResult result = new SimulationResult(createCellData());
				result.setSimulationTime(i * 10);
				metrics.addResult(result);
			}
			
//			assertEquals();
		} catch (QueryBoundaryException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDisplay(){
		boolean printHeader = true;
		List<Cell[]> allCells = metrics.getAll();
		int columnCount = 0;
		for (Cell[] cells : allCells) {
			columnCount = cells.length;
			Long simTime = metrics.getSimTimes().get(allCells.indexOf(cells));
			if(printHeader){
				for (int i = 0; i < cells.length; i++) {
					//prindt column headers
					System.out.print(cells[i].getLongitude() + "-" + cells[i].getLatitude() + "\t");
				}
				System.out.println("MEAN\n");
				printHeader = false;
			}
			//print row header
			System.out.print(simTime);
			for (int i = 0; i < cells.length; i++) {
				System.out.print("\t" + cells[i].getTemperature());
			}
			System.out.print("\t | " + metrics.getMeanForRegion(simTime) + "\n");
		}
		System.out.print("mean\t");
		for (int i = 0; i < columnCount; i++) {
			System.out.print(metrics.getMeanForTime(i) + "\t");
		}
	}
	
	private Cell[][] createCellData() {
		Cell[][] cells = new Cell[5][5];
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Cell cell = new Cell();
				cell.setLongitude(-180d + (72 * i));
				cell.setLatitude(-90d + (36 * j));
				cell.setTemperature(((i * 5) + j) * 100d);
				cells[i][j] = cell;
			}
		}
		return cells;
	}
}
