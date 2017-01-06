package br.com.e7.exploringmars.exception;

public class InvalidCoordinateException extends RuntimeException {

	private static final long serialVersionUID = -5247128985110195649L;

	public InvalidCoordinateException(final String message) {
		super(message);
	}

}