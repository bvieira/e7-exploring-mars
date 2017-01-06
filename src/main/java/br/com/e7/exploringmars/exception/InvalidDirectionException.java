package br.com.e7.exploringmars.exception;

public class InvalidDirectionException extends ApplicationException {

	private static final long serialVersionUID = -2730370428395613383L;
	
	public InvalidDirectionException(final String msg) {
		super(msg);
	}
	
	@Override
	public ErrorType getType() {
		return ErrorType.VALIDATION;
	}

}
