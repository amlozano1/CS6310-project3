package base;

import java.util.logging.Logger;

import exceptions.ArgumentInvalidException;

public final class Utils {

	private final static Logger LOGGER = Logger.getLogger(Utils.class.getName());
	
	// TODO: Lookup the max value for our data implementation
	private static final int MAX_PRECISION_VALUE = 10;

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
			if (args[i] == "-p") {
				try {
					int precision = Integer.parseInt(args[i + 1]);
					if (precision < 0 || precision > MAX_PRECISION_VALUE) {
						throw new ArgumentInvalidException("-p", "-p must be a valid integer value from 0 to " + MAX_PRECISION_VALUE);
					}
					parsedParms.precision = precision;
					i++;
				} catch (NumberFormatException ex) {
					throw new ArgumentInvalidException("-p", "-p must be a valid integer value from 0 to " + MAX_PRECISION_VALUE, ex);
				}
			} else if (args[i] == "-g") {
				try {
					int geographicPrecision = Integer.parseInt(args[i + 1]);
					if (geographicPrecision < 0 || geographicPrecision > 100) {
						throw new ArgumentInvalidException("-g", "-g must be a valid integer value from 0 to 100");
					}
					parsedParms.geographicPrecision = geographicPrecision;
					i++;
				} catch (NumberFormatException ex) {
					throw new ArgumentInvalidException("-g", "-g must be a valid integer value from 0 to 100", ex);
				}
			} else if (args[i] == "-t") {
				try {
					int temporalPrecision = Integer.parseInt(args[i + 1]);
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
		return parsedParms;
	}
	
	/**
	 * Holds invocation parameters
	 */
	public static class InvocationParms {
		/**
		 * -p #
		 */
		public int precision = 0;
		
		/**
		 * -g #
		 */
		public int geographicPrecision = 100;
		
		/**
		 * -t #
		 */
		public int temporalPrecision = 100;
	}

}
