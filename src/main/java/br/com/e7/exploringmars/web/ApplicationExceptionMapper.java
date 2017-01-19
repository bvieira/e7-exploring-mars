package br.com.e7.exploringmars.web;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.e7.exploringmars.exception.ApplicationException;
import br.com.e7.exploringmars.model.Error;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {
	private final Logger logger;
	
	public ApplicationExceptionMapper() {
		logger = LoggerFactory.getLogger(getClass());
	}

	@Override
	public Response toResponse(ApplicationException exception) {
		switch (exception.getType()) {
		case VALIDATION:
			return response(Response.Status.BAD_REQUEST, new StringBuilder(exception.getClass().getSimpleName()).append(" - ").append(exception.getMessage()).toString());
		case REPOSITORY_INDEX:
		case REPOSITORY_SEARCH:
			logger.error("application error, exception:[{}] message:[{}]", exception.getClass().getName(), exception.getMessage());
			return response(Response.Status.INTERNAL_SERVER_ERROR, exception.getMessage());
		default:
			return response(Response.Status.INTERNAL_SERVER_ERROR, exception.getMessage());
		}
		
	}
	
	private Response response(final Status code, final String message) {
		return Response.status(code).type(MediaType.APPLICATION_JSON).entity(new Error(message)).build();
	}

}
