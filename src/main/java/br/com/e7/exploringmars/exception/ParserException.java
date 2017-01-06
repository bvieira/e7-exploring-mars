package br.com.e7.exploringmars.exception;

public class ParserException extends RuntimeException {
	
	private static final long serialVersionUID = -2524272506250680868L;

	public ParserException(final String msg) {
		super(msg);
	}
}