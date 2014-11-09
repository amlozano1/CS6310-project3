package Visualizer;

import java.awt.Color;

/**
 * Use this class to get a color representation of a temperature. 
 * This is implemented as a singleton.
 * 
 * @author Andrew Bernard
 */
public class TemperatureColorPicker {
	private static TemperatureColorPicker instance = null;
	
	private TemperatureColorPicker() {	}
	
	static TemperatureColorPicker getInstance() {
		if(instance == null) {
			instance = new TemperatureColorPicker();
		}
		return instance;
	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("This is a singleton - please use the getInstance() method.");
	}	
	
	/**
	 * Maps a color to the given temperature.
	 * 
	 * @param temperature in kelvin
	 * @return the temperature color
	 */
	Color getColor(int tempKelvin) 
	{
		int tempCelsius = tempKelvin - 273;
		
		if (tempCelsius <= -50) {
			return new Color(0x990099);
		}
		else if (tempCelsius <= -40 && tempCelsius > -50) {
			return new Color(0x000099);
		}
		else if (tempCelsius <= -30 && tempCelsius > -40) {
			return new Color(0x0000FF);
		}
		else if (tempCelsius <= -20 && tempCelsius > -30) {
			return new Color(0x0099FF);
		}
		else if (tempCelsius <= -10 && tempCelsius > -20) {
			return new Color(0x33FFFF);
		}
		else if (tempCelsius <= 0 && tempCelsius > -10) {
			return new Color(0xCCFFFF);
		}
		else if (tempCelsius > 0 && tempCelsius <= 10) {
			return new Color(0xFFFF00);
		}
		else if (tempCelsius > 10 && tempCelsius <= 20) {
			return new Color(0xFF9900);
		}
		else if (tempCelsius > 20 && tempCelsius <= 30) {
			return new Color(0xCC3300);
		}
		else if (tempCelsius > 30 && tempCelsius <= 35) {
			return new Color(0x990000);
		}
		
		return new Color(0xff0000);
		 
  }
	
}
