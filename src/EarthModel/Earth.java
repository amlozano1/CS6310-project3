/*
 * This code came from Wade Ashby's teams Project 2 code
 */
package EarthModel;
import java.util.Date;

//import Visualizer.TemperatureGrid;

public class Earth {//implements TemperatureGrid{
	public static final double RADIUS = 6371;
	public static final double CIRCUMFERENCE = 40030.14;
	public static final double SURFACEAERA = 510072000;
	public static final double INITIALTEMPERATURE = 288;
	public static final double HPSKM = 0.00001; //Heating per square KM by the sun
	public static double CPSKM; //Cooling per square KM by the sun
	private int n_row, n_col;
	private int gs;
	private int timeElapsed = 0;
	private Cell[][] grid;
	private int columnUnderSun;
	
	public Earth(int gs, int timeElapsed) {
		this.gs = gs;
		this.timeElapsed = timeElapsed;
		
		n_row = 180 / gs;
		n_col = 360 / gs;
		
		grid = new Cell[n_row][n_col];
		columnUnderSun = calculateColumnUnderSun();
		double temp = INITIALTEMPERATURE;
		for(int i=0; i < n_row; i++) {
			for(int j=0; j < n_col; j++) {
				grid[i][j] = new Cell(i, j, n_row, n_col, gs, timeElapsed,columnUnderSun);
				grid[i][j].setTemperature(temp);
				//grid[i][j].setColumnUnderSun(columnUnderSun);
			}
		}
		calculateCoolingConstnat();
	}
	
	public Cell getCell(int i, int j) {
		return grid[i][j];
	}
	
	public void displayGrid() {
		for(int i=0; i < n_row; i++) {
			System.out.println();
			for(int j=0; j < n_col; j++) {
				grid[i][j].displayCell();
				System.out.println();
			}
		}
		System.out.printf("Cooling Constant = %.20f", CPSKM);
		System.out.printf("Time Elapsed = %s", timeElapsed);
		System.out.println();
		System.out.println();
	}
	
	public double calculateCoolingConstnat() {
		double sumHeating = 0;
		double sumCooling = 0;
		for(int i=0; i < n_row; i++) {
			for(int j=0; j < n_col; j++) {
				sumHeating += grid[i][j].getArea() * grid[i][j].getHeatingFactor();
				sumCooling += grid[i][j].getTemperature() * grid[i][j].getArea();
			}
		}
		CPSKM = sumHeating * HPSKM / sumCooling;
		return CPSKM;
	}
	
	public Earth iterate(int timestep) {
		Earth newEarth = new Earth(gs, timeElapsed + timestep);
		for(int i=0; i < n_row; i++) {
			for(int j=0; j < n_col; j++) {
				Cell cell = newEarth.getCell(i, j);
				Cell oldCell = grid[i][j];
				
				//heating from the sun
				double tSun = HPSKM * oldCell.getArea() * oldCell.getHeatingFactor();
				
				//cell cooling
				double tCool = CPSKM * oldCell.getArea() * oldCell.getTemperature();
				
				//calculate heating from neighbors
				double pm = oldCell.getPerimeter();
				double pN = oldCell.getTopLength() / pm;
				double pS = oldCell.getBottomLength() / pm;
				double pW = oldCell.getLeftLength() / pm;
				double pE = oldCell.getRightLength() / pm;
				
				double tNeighbors = getNorthTemperature(i, j) * pN + getSouthTemperature(i, j) * pS 
						+ getWestTemperature(i, j) * pW + getEastTemperature(i, j) * pE;
				
				//calculate the new Temperature
				double newTemp = tSun - tCool + tNeighbors;
 				cell.setTemperature(newTemp);
			}
		}		
		return newEarth;
	}
	
	public double getNorthTemperature(int i, int j) {
		if (i + 1 <= n_row - 1) {
			i = i + 1;
		} 
		else {
			j = (j + n_col / 2) % n_col;
		}
		return grid[i][j].getTemperature();
	}
	
	public double getSouthTemperature(int i, int j) {
		if (i == 0) {
			j = (j + n_col / 2) % n_col;
		} 
		else {
			i = i - 1;
		}
		return grid[i][j].getTemperature();
	}
	
	public double getEastTemperature(int i, int j) {
		if (i + 1 <= n_row - 1) {
			i = i + 1;
		} 
		else {
			j = (j + n_col / 2) % n_col;
		}
		return grid[i][j].getTemperature();
	}
	
	public double getWestTemperature(int i, int j) {
		if (i + 1 <= n_row - 1) {
			i = i + 1;
		} 
		else {
			j = (j + n_col / 2) % n_col;
		}
		return grid[i][j].getTemperature();
	}
	
	public int calculateColumnUnderSun( ) {
		double angle = (double) (timeElapsed % 1440) * 360 / 1440;
		int column = (int) Math.floor(angle/gs);
		
		if ( column == 0 ) {
			return 0;
		} else {
			return ((360/gs) + 1 - column);
		}
		//return (int) Math.floor(angle / gs);
	}

	public float getfColumnUnderSun( ) {
		float angle = (float) (timeElapsed % 1440) * 360 / 1440;
		//System.out.println("Sun Angle:" + angle );
		return angle;
	}
	
	public int getElapsedTime( ) {
		return timeElapsed;
	}
	
	//@Override
	public double getTemperature(int x, int y) {
		return grid[y][x].getTemperature();
	}

	//@Override
	public float getCellHeight(int x, int y) {
		return (float)grid[y][x].getHeight();
	}
}