package br.com.e7.exploringmars.exception;

public abstract class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 7960926204402044092L;
	
	public ApplicationException(final String msg) {
		super(msg);
	}
	
	public ApplicationException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
	
	public abstract ErrorType getType();
	
	public static enum ErrorType {
		VALIDATION, REPOSITORY_INDEX, REPOSITORY_SEARCH;
	}
}
