package br.com.e7.exploringmars.exception;

public class RepositorySearchingException extends ApplicationException {

	private static final long serialVersionUID = 1626694798364405837L;

	public RepositorySearchingException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public ErrorType getType() {
		return ErrorType.REPOSITORY_SEARCH;
	}

}