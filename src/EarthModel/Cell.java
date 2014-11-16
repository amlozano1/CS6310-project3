/*
 * This code came from Wade Ashby's teams Project 2 code
 */
package EarthModel;

public class Cell {
	private double lat;
	private double lng;
	private int gs;
	private int i;
	private int j;
	private int n_row;
	private int n_col;
	private double temperature;
	private double topLength;
	private double bottomLength;
	private double leftLength;
	private double rightLength;
	private double height; 
	private double pm; //perimeter of the cell
	private double area;
	private double ra; //proportion of the Earth's surface area taken by the cell
	private double heatingFactor;
	
	Cell(int i, int j, int n_row, int n_col, int gs, int timeElapsed, int colUnderSun) {
		this.gs = gs;
		this.i = i;
		this.j = j;
		this.n_row = n_row;
		this.n_col = n_col;
		lat = (double) (i - (n_row/2)) * gs;
		lng = (double) j < n_col/2 ? -gs * j : (n_col - j) * gs;
		
		leftLength = rightLength = Earth.CIRCUMFERENCE * gs / 360.0;
		topLength = 2 * Math.PI * Earth.RADIUS * Math.cos(Math.toRadians(lat + gs)) * gs / 360.0;
		bottomLength = 2 * Math.PI * Earth.RADIUS * Math.cos(Math.toRadians(lat)) * gs / 360.0;
		height = Math.sqrt(Math.pow(leftLength, 2) - Math.pow((bottomLength - topLength), 2) / 4);
		pm = topLength + bottomLength + leftLength + rightLength;
		area = (topLength + bottomLength) * height / 2;
		ra = area / Earth.SURFACEAERA;
		//colUnderSun = getColumnUnderSun(timeElapsed, gs);
		calculateHeatingFactor(colUnderSun);
	}
	
	public void calculateHeatingFactor(int colUnderSun) {
		double factorLat = Math.cos(Math.toRadians(lat));
		double factorLng = 0;
		int colDelta = j - colUnderSun;
		if ( colDelta >= 0 && colDelta < n_col / 4) {
			factorLng = Math.cos(Math.toRadians(colDelta * gs));
		} else if (colDelta < -n_col * 3 / 4) {
			factorLng = Math.cos(Math.toRadians((n_col + colDelta) * gs));
		} else if (colDelta >= -n_col / 4 && colDelta < 0) {
			factorLng = Math.cos(Math.toRadians((1 + colDelta) * gs));
		}  else if (colDelta >= n_col * 3 / 4  ) {
			factorLng = Math.cos(Math.toRadians((n_col - colDelta - 1) * gs));
		} else {
			factorLng = 0;
		}
		heatingFactor =  factorLat * factorLng;
	}
	
	public double getHeatingFactor() {
		return heatingFactor;
	}
	
	public double calculateCoolingFactor() {
		return 0;
	}
	
	public double getTemperature()
	{
		return temperature;
	}
	
	public void setTemperature(double value)
	{
		temperature = value;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLng() {
		return lng;
	}
	
	public void displayCell() {
		System.out.printf("lat=%.0f, lng=%.0f, side=%.1f, bottom=%.1f, top=%.1f, h=%.1f, pm=%.1f, area=%.1f, ra=%.6f, hf=%.3f, temp=%.3f", 
							lat, lng, leftLength, bottomLength, topLength, height, pm, area, ra, heatingFactor, temperature);
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public double getArea() {
		return area;
	}
	
	public double getPerimeter() {
		return pm;
	}
	
	public double getTopLength(){
		return topLength;
	} 
	
	public double getBottomLength(){
		return bottomLength;
	} 
	
	public double getLeftLength(){
		return leftLength;
	} 
	
	public double getRightLength(){
		return rightLength;
	} 
	
//	public int getColumnUnderSun(int timeElapsed, int gs) {
//		double angle = (double) (timeElapsed % 1440) * 360 / 1440;
//		int column = (int) Math.floor(angle/gs);
//		
//		if ( column == 0 ) {
//			return 0;
//		} else {
//			return ((360/gs) + 1 - column);
//		}
//		//return (int) Math.floor(angle / gs);
//	}
//	
//	public void setColumnUnderSun(int columnUnderSun) {
//		colUnderSun = columnUnderSun;
//	}
}