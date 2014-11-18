package simulation;

public class CellCalculations {
	
	public static double getCellHeight(double circumference, double gridSpacing) {
		double p = gridSpacing / 360;
		return circumference * p;
	}
	
	public static int getNumberOfColumns(double gridSpacing) {
		return (int)(360 / gridSpacing);
	}
	
	public static int getNumberOfRows(double gridSpacing) {
		return (int)(180 / gridSpacing);
	}

	public static double getLatitudeOfCellsInRow(int row, double gridSpacing) {
		double totalRows = getNumberOfRows(gridSpacing);
		return (row - (totalRows / 2.0)) * gridSpacing;
	}
	
	public static double getLongitudeOfCellsInColumn(int column, double gridSpacing) {
		double totalColumns = getNumberOfColumns(gridSpacing);
		double d = (column + 1) * gridSpacing;
		return column < (totalColumns / 2) ? -d : 360 - d;
	}
	
	public static double getBaseWidth(int row, double circumference, double gridSpacing) {
		double latitude = getLatitudeOfCellsInRow(row, gridSpacing);
		double height = getCellHeight(circumference, gridSpacing);
		return Math.cos(getRadiansFromDegrees(latitude)) * height;
	}
	
	public static double getCeilingWidth(int row, double circumference, double gridSpacing) {
		double latitude = getLatitudeOfCellsInRow(row, gridSpacing);
		double height = getCellHeight(circumference, gridSpacing);
		return Math.cos(getRadiansFromDegrees(latitude + gridSpacing)) * height;
	}
	
	public static double getAltitude(int row, double circumference, double gridSpacing) {
		double height = getCellHeight(circumference, gridSpacing);
		double base = getBaseWidth(row, circumference, gridSpacing);
		double ceiling = getCeilingWidth(row, circumference, gridSpacing);
		return Math.sqrt((height * height) - (1 / 4) * (base - ceiling) * (base - ceiling));
	}
	
	public static double getPerimeter(int row, double circumference, double gridSpacing) {
		double height = getCellHeight(circumference, gridSpacing);
		double base = getBaseWidth(row, circumference, gridSpacing);
		double ceiling = getCeilingWidth(row, circumference, gridSpacing);
		return ceiling + base + 2 * height;
	}
	
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
	 * @param column
	 * @param circumference
	 * @param gridSpacing
	 * @return
	 */
	public static double getDistanceFromPrimeMeridian(int column, double circumference, double gridSpacing) {
		double columnCount = getNumberOfColumns(gridSpacing);
		return (((column + (columnCount / 2.0)) % columnCount) - ((columnCount / 2.0) - 1.0)) * circumference / columnCount;
	}
	
	/**
	 * Gets the row distance North of the Equator.
	 * 
	 * @param row
	 * @param circumference
	 * @param gridSpacing
	 * @return
	 */
	public static double getDistanceFromEquator(int row, double circumference, double gridSpacing) {
		double rowCount = getNumberOfRows(gridSpacing);
		return (row - (rowCount / 2.0)) * circumference / rowCount;
	}
	
	public static double getRadiansFromDegrees(double degrees) {
		return degrees * Math.PI / 180;
	}
	
	public static double getRotationalAngle(int time) {
		return (time % 1440) * 360 / 1440;
	}
	
	public static double getHeatAttenuation(int row, int column, double gridSpacing, int time) {
		// TODO: Account for tilt here? then apply to rotationAngle
		double rotationalAngle = getRotationalAngle(time);
		double latitude = getLatitudeOfCellsInRow(row, gridSpacing);
		double longitude = getLongitudeOfCellsInColumn(column, gridSpacing);
		double d = longitude >= 0 ? 360 - longitude : -longitude;
		double dNoon = Math.abs(d - rotationalAngle);
		double a = dNoon < 90 ? Math.cos(dNoon) : 0;
		return Math.cos(latitude) * a;
	}
	
	public static double getBorderEastWestProportion(int row, double circumference, double gridSpacing) {
		return getCellHeight(circumference, gridSpacing) / getPerimeter(row, circumference, gridSpacing);
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
	
	public static double getSolarHeat(int row, int column, double gridSpacing, int time, double circumference, double solarPowerPerMeter) {
		double attenuation = getHeatAttenuation(row, column, gridSpacing, time);
		double area = getArea(row, circumference, gridSpacing);
		double solarPower = solarPowerPerMeter * area;
		return solarPower + (attenuation * solarPower);
	}
	
	public static double getCooling(int row, double circumference, double gridSpacing, double cellTemp, double averageTemp, double solarPowerPerMeter) {
		double radius = circumference / (2 * Math.PI);
		double area = 4 * Math.PI * radius * radius;
		double gridSize = getNumberOfRows(gridSpacing) * getNumberOfColumns(gridSpacing);
		double averageSize = area / gridSize;
		double cellArea = getArea(row, circumference, gridSpacing);
		double relativeSizeFactor = cellArea / averageSize;
		return -relativeSizeFactor * (cellTemp / averageTemp) * solarPowerPerMeter;
	}
	
}
