package base;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

import exceptions.ArgumentInvalidException;

public final class Utils {

	private final static Logger LOGGER = Logger.getLogger(Utils.class.getName());
	
	public static String INVOCATION_PARAMETERS_KEY = "InvocationParms";
	
	// TODO: Lookup the max value for our data implementation
	private static final int MAX_PRECISION_VALUE = 10;

	protected static final Calendar START_TIME = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	
	static{
		START_TIME.set(Calendar.YEAR, 2014);
		START_TIME.set(Calendar.MONTH, Calendar.JANUARY);
		START_TIME.set(Calendar.DAY_OF_MONTH, 4);
		START_TIME.set(Calendar.HOUR_OF_DAY, 0);//noon
		START_TIME.set(Calendar.MINUTE, 0);
		START_TIME.set(Calendar.SECOND, 0);
		START_TIME.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * Private constructor is used to prevent the default constructor from being public.
	 * This makes the entire class static.
	 */
	private Utils() { }
	
	/**
	 * Parses the command line arguments and returns them in the form of an InvocationParms object. 
	 * 
	 * @param args The command line arguments to parse
	 * @return An InvocationParms object containing the parsed arguments
	 * @throws ArgumentInvalidException Thrown if an argument is invalid
	 */
	public static InvocationParms parseArguments(String[] args) throws ArgumentInvalidException {
		InvocationParms parsedParms = new InvocationParms();
		for (int i = 0; i < args.length; i++) {
			if ("-p".equals(args[i])) {
				try {
					short precision = Short.parseShort(args[i + 1]);
					if (precision < 0 || precision > MAX_PRECISION_VALUE) {
						throw new ArgumentInvalidException("-p", "-p must be a valid integer value from 0 to " + MAX_PRECISION_VALUE);
					}
					parsedParms.precision = precision;
					i++;
				} catch (NumberFormatException ex) {
					throw new ArgumentInvalidException("-p", "-p must be a valid integer value from 0 to " + MAX_PRECISION_VALUE, ex);
				}
			} else if ("-g".equals(args[i])) {
				try {
					short geographicPrecision = Short.parseShort(args[i + 1]);
					if (geographicPrecision < 0 || geographicPrecision > 100) {
						throw new ArgumentInvalidException("-g", "-g must be a valid integer value from 0 to 100");
					}
					parsedParms.geographicPrecision = geographicPrecision;
					i++;
				} catch (NumberFormatException ex) {
					throw new ArgumentInvalidException("-g", "-g must be a valid integer value from 0 to 100", ex);
				}
			} else if ("-t".equals(args[i])) {
				try {
					short temporalPrecision = Short.parseShort(args[i + 1]);
					if (temporalPrecision < 0 || temporalPrecision > 100) {
						throw new ArgumentInvalidException("-t", "-t must be a valid integer value from 0 to 100");
					}
					parsedParms.temporalPrecision = temporalPrecision;
					i++;
				} catch (NumberFormatException ex) {
					throw new ArgumentInvalidException("-t", "-t must be a valid integer value from 0 to 100", ex);
				}
			}
		}
		LOGGER.info("Precision: " + parsedParms.precision);
		LOGGER.info("Geographic Precision: " + parsedParms.geographicPrecision);
		LOGGER.info("Temporal Precision: " + parsedParms.temporalPrecision);
		
		System.getProperties().put(INVOCATION_PARAMETERS_KEY, parsedParms);

		return parsedParms;
	}
	
	/**
	 * Holds invocation parameters
	 */
	public static class InvocationParms {
		/**
		 * -p #
		 */
		public short precision = 0;
		
		/**
		 * -g #
		 */
		public short geographicPrecision = 100;
		
		/**
		 * -t #
		 */
		public short temporalPrecision = 100;
	}

	public static final Time toTime(long simulationTime) {
		Calendar c = getForMinutesSinceStart((int) simulationTime);
		return new Time(c.getTimeInMillis());
	}

	public static final Date toDate(long simulationTime) {
		Calendar c = getForMinutesSinceStart((int) simulationTime);
		return new Date(c.getTimeInMillis());
	}
	
	public static final long toSimulationTime(java.util.Date date) {
		long millisChoosen = date.getTime() + TimeZone.getDefault().getRawOffset();
		long millisElapsed = millisChoosen - START_TIME.getTimeInMillis();
		long secondsElapsed = millisElapsed / 1000;
		long minutesElapsed = secondsElapsed / 60;
		return minutesElapsed;
	}
	
	public static final double setPrecision(double value, int decimalPlaces){
		BigDecimal bd = new BigDecimal(value);
		return bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private static final Calendar getForMinutesSinceStart(int minutesSinceStart){
		Calendar c = (Calendar)START_TIME.clone();
		c.add(Calendar.MINUTE, minutesSinceStart);
		
		return c;
	}
}
