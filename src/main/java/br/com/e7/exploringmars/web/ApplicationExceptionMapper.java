package br.com.e7.exploringmars.web;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.e7.exploringmars.exception.ApplicationException;
import br.com.e7.exploringmars.model.Error;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException>{

	@Override
	public Response toResponse(ApplicationException exception) {
		switch (exception.getType()) {
		case VALIDATION:
			return response(Response.Status.BAD_REQUEST, exception.getMessage());
		default:
			return response(Response.Status.INTERNAL_SERVER_ERROR, exception.getMessage());
		}
		
	}
	
	private Response response(final Status code, final String message) {
		return Response.status(code).type(MediaType.APPLICATION_JSON).entity(new Error(message)).build();
	}

}
