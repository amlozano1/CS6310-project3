package base;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimum temperature in the region, when and where it occurred; that is, the
 * smallest temperature in the entire table and the time and location where it
 * occurred 
 * = double temp, double long, double lat, long simTime 
 * 
 * Maximum temperature in the region, when and where it occurred; that is, the
 * largest temperature in the table and the time and location where it occurred
 *  = double temp, double long, double lat, long simTime 
 *  
 * Mean temperature over the region for the requested times; that is, for each
 * row in the table, what was its mean temperature across all of the columns.
 * (The denominator is the number of columns.)
 * 
 * Mean temperature over the times for the requested region; that is, for each
 * column in the table, what was its mean temperature down all rows. (The
 * denominator is the number of rows.)
 * 
 * Actual values, whether recorded or interpolated, for the requested cells at
 * the requested times. In this case, temperature values for all grid cells in
 * the region should be reported. Moreover, values for all computed time steps
 * should be reported. That is, the entire table is being reported on
 * 
 * @author ahigdon
 * 
 */
public class QueryMetrics {
	private Cell minTemp;
	private Cell maxTemp;
	private long minTime;
	private long maxTime;
	private List<Cell[]> all;
	private List<Long> simTimes;
	private List<String> regions;
	
	private static final QueryMetrics metrics = new QueryMetrics();
	
	public static final QueryMetrics getInstance(){
		return metrics;
	}
	
	private QueryMetrics(){
		clear();
	}
	
	public void clear(){
		minTemp = null;
		maxTemp = null;
		minTime = 0;
		maxTime = 0;
		all = new ArrayList<Cell[]>();
		simTimes = new ArrayList<Long>();
		regions = new ArrayList<String>();	
	}
	
	public void addResult(SimulationResult result){
		Cell[][] cellData = result.getResultData();
		List<Cell> flatTempData = new ArrayList<Cell>();
		simTimes.add(result.getSimulationTime());
		for (int row = 0; row < cellData.length; row++) {
			for (int column = 0; column < cellData[0].length; column++) {
				Cell cell = cellData[row][column];
				checkMin(result, cell);
				checkMax(result, cell);
				flatTempData.add(cell);
				regions.add(row + " x " + column);
			}
		}
		all.add(flatTempData.toArray(new Cell[flatTempData.size()]));
	}
	
	public Double getMeanForRegion(Long simulationTime){
		int index = simTimes.indexOf(simulationTime);
		if(index >= 0){
			return average(all.get(index));
		}
		
		return null;
	}
	
	public Double getMeanForTime(int index){
		double total = 0;
		for (Cell[] region : all) {
			total+=region[index].getTemperature();
		}
		
		return total/all.size();
	}
	
	public List<Cell[]> getAll() {
		return all;
	}

	public List<Long> getSimTimes() {
		return simTimes;
	}

	public Cell getMax(){
		return maxTemp;
	}
	
	public Cell getMin(){
		return minTemp;
	}
	
	public long getMinTime() {
		return minTime;
	}

	public long getMaxTime() {
		return maxTime;
	}

	private Double average(Cell[] doubles) {
		double total = 0;
		for (int i = 0; i < doubles.length; i++) {
			total += doubles[i].getTemperature();
		}
		return total/doubles.length;
	}

	private void checkMax(SimulationResult result, Cell cell) {
		if(maxTemp == null || cell.getTemperature() > maxTemp.getTemperature()){
			maxTemp = cell;
			maxTime = result.getSimulationTime();
		}		
	}

	private void checkMin(SimulationResult result, Cell cell) {
		if(minTemp == null || cell.getTemperature() < minTemp.getTemperature()){
			minTemp = cell;
			minTime = result.getSimulationTime();
		}
	}
}
