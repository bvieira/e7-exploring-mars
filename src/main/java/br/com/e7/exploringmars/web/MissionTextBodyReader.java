package br.com.e7.exploringmars.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import br.com.e7.exploringmars.exception.ParserException;
import br.com.e7.exploringmars.model.Action;
import br.com.e7.exploringmars.model.Direction;
import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.Rover;
import br.com.e7.exploringmars.model.Rover.CoordinateValidation;

@Provider
@Consumes(MediaType.TEXT_PLAIN)
public class MissionTextBodyReader implements MessageBodyReader<Mission> {
	
	@Context
    private UriInfo uriInfo;

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == Mission.class;
	}

	@Override
	public Mission readFrom(Class<Mission> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		return parseMission(uriInfo.getPathParameters().getFirst("name"), entityStream);
	}
	
	Mission parseMission(final String name, final InputStream entityStream) {
		final Mission mission = new Mission(name);
		try(BufferedReader bufReader = new BufferedReader(new InputStreamReader(entityStream))) {
			final int[] surfaceValues = parseSurfaceValues(bufReader.readLine());
			mission.setSurfaceWidth(surfaceValues[0]);
			mission.setSurfaceHeight(surfaceValues[1]);
			
			final CoordinateValidation validation = Rover.createSimpleCordinateValidation(mission.surfaceWidth(), mission.surfaceHeight());
			for(String line = bufReader.readLine(); line != null; line = bufReader.readLine()) {
				mission.addRoverMission(parseRoverMission(line, bufReader.readLine(), validation));
			}
		} catch (IOException e) {
			throw new ParserException("could not parse mission text");
		}
		return mission;
	}
	
	private int[] parseSurfaceValues(final String line) {
		final String[] values = line.trim().split(" ");
		if(values.length != 2)
			throw new ParserException("invalid format for surface dimension, missing values");
		
		try {
			return new int[]{Integer.parseInt(values[0]), Integer.parseInt(values[1])};
		} catch(NumberFormatException e) {
			throw new ParserException("invalid format for surface dimension, could not parse to int");
		}
	}
	
	private RoverMission parseRoverMission(final String roverPosition, final String roverAction, final CoordinateValidation validation) {
		if(roverPosition == null || roverAction == null)
			throw new ParserException("invalid format for rover info, position or action is missing");
		return new RoverMission(parseRover(roverPosition, validation), parseRoverActions(roverAction));
	}
	
	private Rover parseRover(final String roverPosition, final CoordinateValidation validation) {
		final String[] values = roverPosition.trim().split(" ");
		if(values.length != 3)
			throw new ParserException("invalid format for rover info, initial position is invalid");
		try{
			return new Rover(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Direction.get(values[2].charAt(0)), validation);
		} catch (IllegalArgumentException e) {
			throw new ParserException("invalid format for rover info, could not parse position to int or direction is invalid");
		}
	}
	
	private List<Action> parseRoverActions(final String roverAction) {	
		try {
			return roverAction.chars().mapToObj(c -> Action.get((char)c)).collect(Collectors.toList());
		} catch(IllegalArgumentException e) {
			throw new ParserException("invalid format for rover info, could not parse actions");
		}
		
	}

	
}