package br.com.e7.exploringmars.exception;

public class InvalidDirectionException extends RuntimeException {

	private static final long serialVersionUID = -2730370428395613383L;
	
	public InvalidDirectionException(final String msg) {
		super(msg);
	}

}
