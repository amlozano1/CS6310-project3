import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class SimulationEngine{
	private SimulationBuffer buffer;
	private SimulationOptions options;
	private float currentSunRotationalPosition;
	private Date currentDateTime;
	private float[][] cells;	// current cell values needed for neighbor radiation
	private float[][] newCells; // new cell values after calculations
	private int iteration = 0;

	public SimulationEngine(SimulationBuffer buffer, SimulationOptions options) {
		this.buffer = buffer;
		this.options = options;
		cells = new float[options.getGridHeight()][options.getGridLength()];
		
		currentDateTime = options.getInitialDateTime();
		
		// set initial values
		setCellsDefault();
		newCells = cells; 
		currentSunRotationalPosition = options.getInitialSunPosition();
		// TODO: Other constructor init. No business logic here please.
		// Use the options above to set anything that needs set or you can use the options object directly.
	}

	/**
	 * Set the initial cell temperature to the value set in options
	 */
	private void setCellsDefault() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j] = options.getInitialCellTemperatureInKelvin();
			}
		}
	}

	@Override
	public SimulationStepResult moveStep() {
		if(buffer.isFull()){
			return new IdleSimulationStepResult();
		}
		
		/*
		 *  Step 1: Turn the earth the appropriate amount of minutes based on passed in options.
		 */
		currentSunRotationalPosition = Util.getSunRotationalPosition(currentSunRotationalPosition, options.getTimeStepInMinutes());
		currentDateTime = Util.getTimeAfterStep(currentDateTime, options.getTimeStepInMinutes());
		
		// *  Step 2: Calculate the temperature of every cell.
		simulate();
		
		/*  Step 3: Create a SimulationStepResult object. Don't worry about instrumentation fields such as idle time 
		 *  or memory usage. These are handled elsewhere.
		 */
		iteration++;
		SimulationStepResult stepResult = new SimulationStepResult();
		stepResult.setCellTemperatures(cells);
		stepResult.setSunRotationalPosition(currentSunRotationalPosition);
		stepResult.setIteration(iteration);
		stepResult.setCalculationDate(currentDateTime);
		//*  Step 4: Use the buffer object above and push() the step result to it.
		buffer.push(stepResult);

		//*  Step 5: Return the result for instrumentation
		return stepResult;
	}
	
	/**
	 * Calculate the temperatures of the cells for the current time/location
	 */
	private void simulate() {
		
		int rows = options.getGridHeight();
		int cols = options.getGridLength();
		int gridSpacing = options.getGridSpacing();
		
		// calculate the time in minutes since the simulation started
		long minutesSinceStart = (currentDateTime.getTime() / 60000) - (options.getInitialDateTime().getTime() / 60000);

		float latitude, longitude, perimeter, area;
		float topLength, baseLength, sideLength, height;
		float northProRating, southProRating, sideProRating;
		
        for (int row = 0; row < rows; row++) {
        	// Most of the calculations are the same for each row
        	// do them once per row and use the results on the columns
      	  	latitude = Util.getLatitude(row, gridSpacing);
      	  	sideLength = Util.getSideLengthOfCell(gridSpacing);
      	  	baseLength = Util.getBaseLengthOfCell(row, gridSpacing, latitude, sideLength);
      	  	topLength = Util.getTopLengthOfCell(row, gridSpacing, latitude, sideLength);

      	  	// needed for neighbor calculation
      	  	perimeter = baseLength + topLength + sideLength * 2;
      	  	if (latitude > 0) { // northern hemisphere
      	  		northProRating = topLength / perimeter;
      	  		southProRating = baseLength / perimeter; // base is the southern edge 
      	  	}
      	  	else { // southern hemisphere
      	  		northProRating = baseLength / perimeter; // base is the northern edge
      	  		southProRating = topLength / perimeter;
      	  	}
        	sideProRating = (1-southProRating-northProRating)/2; // to help account for rounding errors, need to = 100%

        	// needed for other two calculations
      	  	height = Util.getHeightOfCell(sideLength, baseLength, topLength);
      	  	area = Util.getAreaOfCell(topLength,baseLength,height);
      	  	
            for (int col = 0; col < cols; col++) {
            	longitude = Util.getLongitude(col, gridSpacing);

            	// pull the values from each of the appropriate cells
            	// account for global wrapping
        		// apply radiant heat from neighboring cells
        		radiantHeatFromNeighbors(row, col, northProRating, southProRating, sideProRating);
        		
        		// apply the radiant heat from the sun
        		float heatFromSun = externalEnergySource(row, col, longitude, latitude, area, minutesSinceStart);
        		
        		// apply the cooling affect by simply doing the opposite of externalEnergySource
        		cooling(row, col, heatFromSun);
            }
        }
        // Swap the plates and continue
        cells = newCells;
//        printCells(); // For debug only
	}
	
	/**
	 * Method to assist debugging by viewing the cells at various states.
	 */
	private void printCells() {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("cellvalues.csv", true)));
			for (int i = 0; i < cells.length-1; i++) {
				writer.print("[");
				for (int j = 0; j <cells[i].length-1; j++) {
					writer.print(cells[i][j] + ",");
				}
				writer.print("]\n");
			}
			writer.println();
			writer.close();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * A method that calculate the percentage contribution of the neighboring cells
	 * based on the position in the grid. Cells towards the top will have narrow tops and cells 
	 * towards the bottom will have narrow bottoms to simulate a sphere shape.
	 * Because the earth doesn't wobble and is perfectly spherical, we can do the math based on 
	 * location in the grid.
	 * @param row
	 * @param col
	 * @param northProRating
	 * @param southProRating
	 * @param sideProRating
	 */
	private void radiantHeatFromNeighbors(int row, int col, float northProRating, float southProRating, float sideProRating) {
		/* This means that the shape
		 *  of a grid cell is no longer rectangular. You may still assume that each cell has four
		 *  neighbors. But in calculating the new temperature for each cell, the contribution of 
		 *  a neighbor is pro-rated by the extent of the cell's common boundary with it.
		 */
		float northCell, southCell, eastCell, westCell;
		int cols = options.getGridLength();
		
    	// pull the values from each of the appropriate cells
    	// account for global wrapping
    	if (row+1 <= options.getGridHeight()-1) {
    		northCell = cells[row+1][col];
    	}
    	else {
    		// wraps around globe 'edge'
    		northCell = cells[row][(col+(cols/2))%cols];
    	}
    	if (row-1 >= 0) {
    		southCell = cells[row-1][col];
    	}
    	else {
    		// wraps around globe 'edge'
    		southCell = cells[row][(col+cols/2)%cols];
    	}
    	eastCell = cells[row][(col-1+cols)%cols];
    	westCell = cells[row][(col+1)%cols];

    	// average the neighboring cell temperature accounting for the 
    	// proportion of the touching edge length
        newCells[row][col] = northCell*northProRating + southCell*southProRating +
                eastCell*sideProRating + westCell*sideProRating;
	}
	
	/**
	 * Method to calculate the percentage of the sun's radiation that actually 
	 * hits the cell.  Something about the incident angle of radiation. Higher north or south
	 * As well as the far east and west from the sun's position will be much less than noon
	 * on the equator (100%) duh.
	 * @param row
	 * @param col
	 * @param area
	 * @param minutesFromStart
	 */
	private float externalEnergySource(int row, int col, float longitude, float latitude, float area, long minutesSinceStart) {
		/*
		 *  Because of the new topology, there are no longer any edges to supply heat. 
		 *  Because there are no such edges, instead grid cells receive externally supplied 
		 *  energy during each time period. This takes the form of an energy source (the Sun) 
		 *  at a given distance from Earth and with a given radiation output. 
		 *  
		 *  At a first approximation, the heat obtained from the Sun is 
		 *  proportional to the surface area of the cell.
		 */
		float heatAttenuation = Util.getHeatAttentuation(minutesSinceStart, longitude, latitude);
		
		float heatFromSun = (float) Math.pow(Util.solarConstant/(Util.stefanBoltzmannConstant*Util.earthEmissivity),.25)*(area/Util.surfaceAreaVisibleBySun)*heatAttenuation;
		
		newCells[row][col] += heatFromSun; // apply heat
				
		return heatFromSun; // return value so cooling can use the same
	}
	
	/**
	 * Method that calculates the cooling amount (if any) for the cell
	 * This might be absorbed into other methods for calculation
	 * @param row
	 * @param col
	 * @param heatFromSun
	 */
	private void cooling(int row, int col, float heatFromSun) {
		/* Cooling: In the absence of cooling, the irradiated Earth would continue to 
		 * increase in temperature indefinitely. We therefore assume that each cell loses 
		 * a percentage of its heat to space during each time period in proportion to its 
		 * surface area and its current temperature. This is a distinct effect from the 
		 * diffusion of heat to its neighboring grid cells.
		 */
		//Cell Cooling section of Model pdf
		//Tcool = -B x gama x Tsun
		//avg grid cell size = n = A/N; 
		//A= 5.10072x10^14 (SA of Earth) N= rows*cols
		//B = actual grid cell size/ n
		//gama = T0(of cell)/ Tavg(of all cell of grid)
		
		//My understanding...idk if this is close to correct
		//Sum of total cell temp of cooling = Sum of total cell temp from sun
		//Sum of total cell temp of cooling = Const Earth Temp - Sun heating
//		float currentTemp = cells[row][col];
//		newCells[row][col] = Math.abs(currentTemp - Util.solarConstant); //absolute difference
		

		// apply cooling in a custom way to keep "equilibrium" since formulas were removed from domain model sheets
		// by simply applying the opposite of the sun's heat on the other side of the globe
		int cols = cells[row].length;
		int halfwayRoundTheWorld = (int) ((col+Math.floor(cols/2))%cols); // wrap around the world
		newCells[row][halfwayRoundTheWorld] -= heatFromSun; // just cancel it out on the other side of the globe.
	}
		
}
