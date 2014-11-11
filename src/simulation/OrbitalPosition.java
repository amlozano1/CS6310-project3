package simulation;

public abstract class OrbitalPosition {
	
	public static double[] getCoordinates(double eccentricity, double time, double solarYear, double semiMajorAxis) {
		double c = semiMajorAxis * eccentricity;
		double minorRadius = getMinorAxis(eccentricity, semiMajorAxis);
		double eT = getEccentricAnomaly(eccentricity, time, solarYear);
		double x = c + semiMajorAxis * Math.cos(eT);
		double y = minorRadius * Math.sin(eT);
		return new double[] {x, y};
	}
	
	public static double getDistanceFromSun(double eccentricity, double time, double solarYear, double semiMajorAxis) {
		double trueAnomaly = getTrueAnomaly(eccentricity, time, solarYear);
		return semiMajorAxis * ((1 - eccentricity * eccentricity) / (1 + eccentricity * Math.cos(trueAnomaly)));
	}
	
	public static double getMeanAnomaly(double time, double solarYear) {
		return 2.0 * Math.PI * time / solarYear;
	}
	
	public static double getEccentricAnomaly(double eccentricity, double time, double solarYear) {
		double meanAnomaly = getMeanAnomaly(time, solarYear);
		// TODO: Determine accuracy
		return newtonRaphson(eccentricity, meanAnomaly, meanAnomaly, 0.01);
	}
	
	public static double getTrueAnomaly(double eccentricity, double time, double solarYear) {
		double eT = getEccentricAnomaly(eccentricity, time, solarYear);
		double cos_eT = Math.cos(eT);
		return Math.acos((cos_eT - eccentricity) / (1 - eccentricity * cos_eT));
	}
	
	/**
	 * http://en.wikipedia.org/wiki/Kepler%27s_equation#Numerical_approximation_of_inverse_problem
	 * 
	 * @param eccentricity
	 * @param eN
	 * @param mT
	 * @param accuracy
	 * @return
	 */
	private static double newtonRaphson(double eccentricity, double eN, double mT, double accuracy) {
		double fE = eN - eccentricity * Math.sin(eN) - mT;
		double eN1 = eN - (fE / (1 - eccentricity * Math.cos(eN)));
		if (fE > accuracy) {
			return newtonRaphson(eccentricity, eN1, mT, accuracy);
		} else {
			return eN1;
		}
	}
	
	private static double getMinorAxis(double eccentricity, double semiMajorAxis) {
		// TODO: Figure this equation out
		return 0;
	}

}
