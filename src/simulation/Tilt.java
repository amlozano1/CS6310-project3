package simulation;

public abstract class Tilt {
	
	public static double getSunAngle(double time, double solarYear, double timeOfEquinox, double obliquity) {
		double phi = getPhi(time, solarYear, timeOfEquinox);
		return obliquity * Math.sin(phi);
	}

	/**
	 * https://piazza.com/class/hvzpk6p64fqax?cid=505
	 * 
	 * @param time
	 * @param solarYear
	 * @param timeOfEquinox
	 * @return
	 */
	private static double getPhi(double time, double solarYear, double timeOfEquinox) {
		return (time - timeOfEquinox) % solarYear * 2 * Math.PI / solarYear;
	}

}
