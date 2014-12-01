package base;

public class SimulationParameters {
	private short gridSpacing;
	private int timeStep;
	private short length;
	private double axialTilt;
	private double orbitalEccentricity;
	private int precision;
	private short geoPrecision;
	private short tempPrecision;
	
	public short getGridSpacing() {
		return gridSpacing;
	}
	public void setGridSpacing(short gridSpacing) {
		this.gridSpacing = gridSpacing;
	}
	public int getTimeStep() {
		return timeStep;
	}
	public void setTimeStep(int timeStep) {
		this.timeStep = timeStep;
	}
	public short getLength() {
		return length;
	}
	public void setLength(short length) {
		this.length = length;
	}
	public double getAxialTilt() {
		return axialTilt;
	}
	public void setAxialTilt(double axialTilt) {
		this.axialTilt = axialTilt;
	}
	public double getOrbitalEccentricity() {
		return orbitalEccentricity;
	}
	public void setOrbitalEccentricity(double orbitalEccentricity) {
		this.orbitalEccentricity = orbitalEccentricity;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public short getGeoPrecision() {
		return geoPrecision;
	}
	public void setGeoPrecision(short geoPrecision) {
		this.geoPrecision = geoPrecision;
	}
	public short getTempPrecision() {
		return tempPrecision;
	}
	public void setTempPrecision(short tempPrecision) {
		this.tempPrecision = tempPrecision;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(axialTilt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + geoPrecision;
		result = prime * result + gridSpacing;
		result = prime * result + length;
		temp = Double.doubleToLongBits(orbitalEccentricity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + precision;
		result = prime * result + tempPrecision;
		result = prime * result + timeStep;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimulationParameters other = (SimulationParameters) obj;
		if (Double.doubleToLongBits(axialTilt) != Double
				.doubleToLongBits(other.axialTilt))
			return false;
		if (geoPrecision != other.geoPrecision)
			return false;
		if (gridSpacing != other.gridSpacing)
			return false;
		if (length != other.length)
			return false;
		if (Double.doubleToLongBits(orbitalEccentricity) != Double
				.doubleToLongBits(other.orbitalEccentricity))
			return false;
		if (precision != other.precision)
			return false;
		if (tempPrecision != other.tempPrecision)
			return false;
		if (timeStep != other.timeStep)
			return false;
		return true;
	}
}
