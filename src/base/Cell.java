package base;

public class Cell {
	private Double temperature;
	private Double longitude;
	private Double latitude;
	
	public Cell(){
		super();
	}
	
	public Cell(Double temperature, Double longitude, Double latitude) {
		this();
		this.temperature = temperature;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
}
