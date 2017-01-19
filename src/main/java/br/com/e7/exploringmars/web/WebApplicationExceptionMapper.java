package br.com.e7.exploringmars.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.e7.exploringmars.model.Error;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<Exception> {
	private final Logger logger = LoggerFactory.getLogger("exception-handler");

	@Override
	public Response toResponse(Exception exception) {
		if(exception instanceof WebApplicationException)
			return Response.status(((WebApplicationException) exception).getResponse().getStatus()).type(MediaType.APPLICATION_JSON).entity(new Error(exception.getMessage())).build();
		logger.error("uncaught exception", exception);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(new Error(new StringBuilder(exception.getClass().getSimpleName()).append(" - ").append(exception.getMessage()).toString())).build();
	}
	
}
