package br.com.e7.exploringmars.web;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.service.MissionService;

@Path("/mission")
public class ExploringMissionREST {
	
	@Inject
	private MissionService missionService;
	
	@POST
    @Path("/{name}")
    @Consumes({ MediaType.TEXT_PLAIN })
    @Produces({ MediaType.TEXT_PLAIN })
    public Response postMission(final Mission mission) {		
        return Response.status(Response.Status.OK).entity(missionService.process(mission)).build();
    }
}
