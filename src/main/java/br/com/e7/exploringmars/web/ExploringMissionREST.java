package br.com.e7.exploringmars.web;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.e7.exploringmars.service.MissionService;

@Path("/mission")
public class ExploringMissionREST {
	
	@Inject
	private MissionService missionService;
	
    @POST
    @Path("/{name}")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response test(@PathParam("name") final String name) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
