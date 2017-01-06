package br.com.e7.exploringmars.exception;

public abstract class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 7960926204402044092L;
	
	public ApplicationException(final String msg) {
		super(msg);
	}
	
	public abstract ErrorType getType();
	
	public static enum ErrorType {
		VALIDATION;
	}
}
