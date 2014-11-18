package base;

public class Cell {
	private double temperature;
	private double longitude;
	private double latitude;
	
	public Cell(){
		super();
	}
	
	public Cell(double temperature, double longitude, double latitude) {
		this();
		this.temperature = temperature;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
