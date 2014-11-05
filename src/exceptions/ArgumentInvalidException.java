package exceptions;

public class ArgumentInvalidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5738481670707135432L;
	
	private String mArgumentName;
	
	public ArgumentInvalidException(String argumentName) {
		super();
		mArgumentName = argumentName;
	}
	
	public ArgumentInvalidException(String argumentName, String message) {
		super(message);
		mArgumentName = argumentName;
	}
	
	public ArgumentInvalidException(String argumentName, Throwable cause) {
		super(cause);
		mArgumentName = argumentName;
	}
	
	public ArgumentInvalidException(String argumentName, String message, Throwable cause) {
		super(message, cause);
		mArgumentName = argumentName;
	}
	
	public String getArgumentName() {
		return mArgumentName;
	}

}
