package br.com.e7.exploringmars.exception;

public class RepositoryIndexingException extends ApplicationException {

	private static final long serialVersionUID = -8775213800300710802L;

	public RepositoryIndexingException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public ErrorType getType() {
		return ErrorType.REPOSITORY_INDEX;
	}

}