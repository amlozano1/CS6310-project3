package simulation;

public class CellCalculations {
	
	public static final double STEFAN_BOLTZMANN_CONSTANT = 0.0000000567;
	
	/**
	 * Gets the length of a vertical side of a cell in a grid with the specified grid spacing on a planet of the specified circumference in meters.
	 * 
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The height of the vertical side in meters.
	 */
	public static double getCellVerticalSideLength(double circumference, double gridSpacing) {
		double p = gridSpacing / 360;
		return circumference * p;
	}
	
	/**
	 * Gets the number of columns in a grid with the specified grid spacing.
	 * 
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The number of columns in a grid with the specified grid spacing.
	 */
	public static int getNumberOfColumns(double gridSpacing) {
		return (int)(360 / gridSpacing);
	}
	
	/**
	 * Gets the number of rows in a grid with the specified grid spacing.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The number of rows in a grid with the specified grid spacing.
	 */
	public static int getNumberOfRows(double gridSpacing) {
		return (int)(180 / gridSpacing);
	}

	/**
	 * Gets the latitude of a cell in the specified row in degrees.
	 * 
	 * @param row The row to examine
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The latitude of a cell in the specified row in degrees.
	 */
	public static double getLatitudeOfCellsInRow(int row, double gridSpacing) {
		double totalRows = getNumberOfRows(gridSpacing);
		return -(row - (totalRows / 2.0)) * gridSpacing;
	}
	
	/**
	 * Gets the longitude of a cell in the specified column in degrees.
	 * 
	 * @param column The column to examine.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The longitude of a cell in the specified column in degrees.
	 */
	public static double getLongitudeOfCellsInColumn(int column, double gridSpacing) {
		double totalColumns = getNumberOfColumns(gridSpacing);
//		double d = (column + 1) * gridSpacing;
//		return column < (totalColumns / 2) ? -d : 360 - d;
		
		return ((360 / totalColumns) * column) - 180;
	}
	
	/**
	 * Gets the width of the base of a cell in the specified row in meters.
	 * 
	 * @param row The row to examine
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The width of the base of a cell in the specified row in meters.
	 */
	public static double getBaseWidth(int row, double circumference, double gridSpacing) {
		double latitude = getLatitudeOfCellsInRow(row, gridSpacing);
		double height = getCellVerticalSideLength(circumference, gridSpacing);
		return Math.cos(getRadiansFromDegrees(latitude)) * height;
	}
	
	/**
	 * Gets the width of the ceiling (top) of a cell in the specified row in meters.
	 * 
	 * @param row The row to examine
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The width of the ceiling (top) of a cell in the specified row in meters.
	 */
	public static double getCeilingWidth(int row, double circumference, double gridSpacing) {
		double latitude = getLatitudeOfCellsInRow(row, gridSpacing);
		double height = getCellVerticalSideLength(circumference, gridSpacing);
		return Math.cos(getRadiansFromDegrees(latitude - gridSpacing)) * height;
	}
	
	/**
	 * Gets the altitude (height) of a cell in the specified row in meters.
	 * 
	 * @param row The row to examine
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The altitude of a cell in the specified row in meters.
	 */
	public static double getAltitude(int row, double circumference, double gridSpacing) {
		double height = getCellVerticalSideLength(circumference, gridSpacing);
		double base = getBaseWidth(row, circumference, gridSpacing);
		double ceiling = getCeilingWidth(row, circumference, gridSpacing);
		return Math.sqrt((height * height) - (1 / 4) * (base - ceiling) * (base - ceiling));
	}
	
	/**
	 * Gets the length of the perimeter of a cell in the specified row in meters.
	 * 
	 * @param row The row to examine
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The length of the perimeter of a cell in the specified row in meters.
	 */
	public static double getPerimeter(int row, double circumference, double gridSpacing) {
		double height = getCellVerticalSideLength(circumference, gridSpacing);
		double base = getBaseWidth(row, circumference, gridSpacing);
		double ceiling = getCeilingWidth(row, circumference, gridSpacing);
		return ceiling + base + 2 * height;
	}
	
	/**
	 * The area of a cell in the specified row in meters squared.
	 * 
	 * @param row The row to examine
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The area of a cell in the specified row in meters squared.
	 */
	public static double getArea(int row, double circumference, double gridSpacing) {
		double base = getBaseWidth(row, circumference, gridSpacing);
		double ceiling = getCeilingWidth(row, circumference, gridSpacing);
		double altitude = getAltitude(row, circumference, gridSpacing);
		return (1.0 / 2.0) * (ceiling + base) * altitude;
	}
	
	public static double getProportionOfPlanet(int row, double circumference, double gridSpacing, double planetSurfaceArea) {
		return getArea(row, circumference, gridSpacing) / planetSurfaceArea;
	}
	
	/**
	 * Gets the column distance West of the Prime Meridian.
	 * 
	 * @param column The column to examine.
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return
	 */
	public static double getDistanceFromPrimeMeridian(int column, double circumference, double gridSpacing) {
		double columnCount = getNumberOfColumns(gridSpacing);
		return (((column + (columnCount / 2.0)) % columnCount) - ((columnCount / 2.0) - 1.0)) * circumference / columnCount;
	}
	
	/**
	 * Gets the row distance North of the Equator.
	 * 
	 * @param row The row to examine
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return
	 */
	public static double getDistanceFromEquator(int row, double circumference, double gridSpacing) {
		double rowCount = getNumberOfRows(gridSpacing);
		return (row - (rowCount / 2.0)) * circumference / rowCount;
	}
	
	public static double getRadiansFromDegrees(double degrees) {
		return degrees * Math.PI / 180;
	}
	
	/**
	 * Gets the rotational angle at the specified time in degrees.
	 * 
	 * @param time The time to measure the angle for in minutes passed.
	 * @return The rotational angle at the specified time in degrees.
	 */
	public static double getRotationalAngle(int time) {
		return (time % 1440.0) * 360.0 / 1440.0;
	}
	
	/**
	 * Calculates the heat attenuation (reduction factor) for the specified cell at the specified time.
	 * 
	 * @param row The row to examine
	 * @param column The column to examine
	 * @param gridSpacing The grid spacing in degrees.
	 * @param time The time to measure attenuation at in minutes passed.
	 * @return The heat attenuation (reduction factor) for the specified cell at the specified time.
	 */
	public static double getHeatAttenuation(int row, int column, double gridSpacing, int sunPosition) {
		// TODO: Account for tilt here? then apply to rotationAngle
		double rotationalAngle = sunPosition % 360;
		double latitude = getLatitudeOfCellsInRow(row, gridSpacing);
		double longitude = getLongitudeOfCellsInColumn(column, gridSpacing);
		double d = longitude >= 0 ? 360 - longitude : -longitude;
		double dNoon = Math.abs(d - rotationalAngle);
		double a = dNoon < 90 ? Math.cos(dNoon) : 0;
		return Math.cos(latitude) * a;
		
//		double a = dNoon < 90 ? Math.cos(getRadiansFromDegrees(dNoon)) : 0;
//		return Math.cos(getRadiansFromDegrees(latitude)) * a;
	}
	
	public static double getBorderEastWestProportion(int row, double circumference, double gridSpacing) {
		return getCellVerticalSideLength(circumference, gridSpacing) / getPerimeter(row, circumference, gridSpacing);
	}
	
	public static double getBorderNorthProportion(int row, double circumference, double gridSpacing) {
		return getBaseWidth(row, circumference, gridSpacing) / getPerimeter(row, circumference, gridSpacing);
	}
	
	public static double getBorderSouthProportion(int row, double circumference, double gridSpacing) {
		return getCeilingWidth(row, circumference, gridSpacing) / getPerimeter(row, circumference, gridSpacing);
	}
	
	public static double getNeighborHeat(int row, double circumference, double gridSpacing, double northTemp, double southTemp, double eastTemp, double westTemp) {
		double northBorderProportion = getBorderNorthProportion(row, circumference, gridSpacing);
		double southBorderProportion = getBorderSouthProportion(row, circumference, gridSpacing);
		double eastBorderProportion = getBorderEastWestProportion(row, circumference, gridSpacing);
		double westBorderProportion = eastBorderProportion;
		return (northBorderProportion * northTemp) + (southBorderProportion * southTemp) + (eastBorderProportion * eastTemp) + (westBorderProportion * westTemp);
	}
	
	/**
	 * 
	 * 
	 * @param row The row to examine
	 * @param column The column to examine
	 * @param gridSpacing The grid spacing in degrees.
	 * @param time The time to measure attenuation at in minutes passed.
	 * @param circumference The circumference of the planet in meters.
	 * @param solarAverageTempApplied The temperature of solar energy in Kelvin.
	 * @return
	 */
	public static double getSolarHeat(int row, int column, double gridSpacing, int time, double circumference, double solarAverageTempApplied) {
		double attenuation = getHeatAttenuation(row, column, gridSpacing, time);
		double relativeSizeFactor = getRelativeSizeFactor(row, circumference, gridSpacing);
		double solarPower = solarAverageTempApplied * relativeSizeFactor;
		return (solarPower - (attenuation * solarPower));
	}
	
	/**
	 * 
	 * 
	 * @param row The row to examine
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @param cellTemp The current temperature of the cell in degrees.
	 * @param averageTemp The average temperature of a cell in degrees.
	 * @param solarAverageTempApplied The temperature of solar energy in Kelvin.
	 * @return
	 */
	public static double getCooling(int row, double circumference, double gridSpacing,double solarAverageTempApplied) {
		double relativeSizeFactor = getRelativeSizeFactor(row, circumference, gridSpacing);
		return (-relativeSizeFactor * solarAverageTempApplied);
	}
	
	/**
	 * Calculates the planets surface area in meters squared.
	 * 
	 * @param circumference The circumference of the planet in meters.
	 * @return The planet surface area in meters squared.
	 */
	public static double getPlanetSurfaceArea(double circumference) {
		double radius = circumference / (2 * Math.PI);
		return 4 * Math.PI * radius * radius;
	}
	
	/**
	 * Calculates the average cell size of the grid in meters squared.
	 * 
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return The average cell size of the grid in meters squared.
	 */
	public static double getAverageCellSize(double circumference, double gridSpacing) {
		double area = getPlanetSurfaceArea(circumference);
		double gridSize = getNumberOfRows(gridSpacing) * getNumberOfColumns(gridSpacing);
		return area / gridSize;
	}
	
	/**
	 * Calculates a factor representing the relative size of this cell to the rest of the grid cells.
	 * 
	 * @param row The row to examine
	 * @param circumference The circumference of the planet in meters.
	 * @param gridSpacing The grid spacing in degrees.
	 * @return A factor representing the relative size of this cell to the rest of the grid cells.
	 */
	public static double getRelativeSizeFactor(int row, double circumference, double gridSpacing) {
		double averageCellSize = getAverageCellSize(circumference, gridSpacing);
		double cellArea = getArea(row, circumference, gridSpacing);
		return cellArea / averageCellSize;
	}
	
	public static double getKelvinFromSolarEnergy(double wattsPerMeter, double aldebo, double emissivity) {
		return Math.pow(((1 - aldebo) * wattsPerMeter) / (4 * emissivity * STEFAN_BOLTZMANN_CONSTANT), 0.25);
	}
	
}
