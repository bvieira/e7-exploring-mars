package br.com.e7.exploringmars.web;

import static br.com.e7.exploringmars.util.ConfigProperties.DEFAULT_ENCODING;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import br.com.e7.exploringmars.exception.InvalidCoordinateException;
import br.com.e7.exploringmars.exception.ParserException;
import br.com.e7.exploringmars.model.Action;
import br.com.e7.exploringmars.model.Direction;
import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.Rover;

@Provider
@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
public class MissionBodyHandler  implements MessageBodyReader<Mission> {
	private final Gson gson;
	
	@Context
    private UriInfo uriInfo;

	public MissionBodyHandler() {
		gson = new GsonBuilder().create();
	}
	

	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == Mission.class;
	}

	@Override
	public Mission readFrom(Class<Mission> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		if(MediaType.APPLICATION_JSON_TYPE.equals(mediaType))
			return parseMissionJson(uriInfo.getPathParameters().getFirst("name"), entityStream);
		return parseMissionText(uriInfo.getPathParameters().getFirst("name"), entityStream);
	}
	
	// -------- parse text
	Mission parseMissionText(final String name, final InputStream entityStream) {
		try(BufferedReader bufReader = new BufferedReader(new InputStreamReader(entityStream))) {
			final int[] surfaceValues = parseSurfaceValues(bufReader.readLine());
			final Mission mission = new Mission(name, surfaceValues[0], surfaceValues[1]);
			final Set<String> occupiedCoordinates = new HashSet<>();
			
			for(String line = bufReader.readLine(); line != null; line = bufReader.readLine()) {
				final RoverMission roverMission = parseRoverMission(line, bufReader.readLine());
				final String coordinate = toCoordinateString(roverMission.rover().x(), roverMission.rover().y());
				if(occupiedCoordinates.contains(coordinate))
					throw new InvalidCoordinateException(String.format("two rovers cannot occupy the same place, coordinate:(%s) in use", coordinate));
				occupiedCoordinates.add(coordinate);
				mission.addRoverMission(roverMission);
			}
			return mission;
		} catch (IOException e) {
			throw new ParserException("could not parse mission text");
		}
	}
	
	private String toCoordinateString(int x, int y) {
		return new StringBuilder().append(x).append(",").append(y).toString();
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
	
	private RoverMission parseRoverMission(final String roverPosition, final String roverAction) {
		if(roverPosition == null || roverAction == null)
			throw new ParserException("invalid format for rover info, position or action is missing");
		return new RoverMission(parseRover(roverPosition), parseRoverActions(roverAction));
	}
	
	private Rover parseRover(final String roverPosition) {
		final String[] values = roverPosition.trim().split(" ");
		if(values.length != 3)
			throw new ParserException("invalid format for rover info, initial position is invalid");
		try{
			return new Rover(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Direction.get(values[2].charAt(0)));
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
	
	// -------- parse json
	Mission parseMissionJson(final String name, final InputStream entityStream) {
		try (final InputStreamReader streamReader = new InputStreamReader(entityStream, Charset.forName(DEFAULT_ENCODING.value()))) {
			return toMission(name, gson.fromJson(streamReader, MissionInput.class));
		} catch (JsonSyntaxException | IOException e) {
			throw new ParserException(e.getMessage());
		}
	}
	
	private Mission toMission(final String name, final MissionInput input) {
		if(input.surface == null || input.surface.width == null || input.surface.height == null)
			throw new ParserException("missing surface width or height");
		if(input.rovers == null || input.rovers.isEmpty())
			throw new ParserException("rover missing on mission");
		final Mission mission = new Mission(name, input.surface.width, input.surface.height);
		final Set<String> occupiedCoordinates = new HashSet<>();
		input.rovers.forEach(r -> {
			final RoverMission roverMission = toRoverMission(r);
			final String coordinate = toCoordinateString(roverMission.rover().x(), roverMission.rover().y());
			if(occupiedCoordinates.contains(coordinate))
				throw new InvalidCoordinateException(String.format("two rovers cannot occupy the same place, coordinate:(%s) in use", coordinate));
			occupiedCoordinates.add(coordinate);
			mission.addRoverMission(roverMission);
		});
		
		return mission;
	}
	
	private RoverMission toRoverMission(final RoverInput input) {
		if(input.x == null || input.y == null || input.direction == null)
			throw new ParserException("rover position is missing");
		if(input.actions == null || input.actions.isEmpty())
			throw new ParserException("rover actions is missing");
		return new RoverMission(new Rover(input.x, input.y, input.direction), input.actions);
	}
	
	private static class MissionInput {
		private SurfaceInput surface;
		private List<RoverInput> rovers;
	}
	
	private static class RoverInput {
		private Integer x;
		private Integer y;
		private Direction direction;
		private List<Action> actions;
	}
	
	private static class SurfaceInput {
		private Integer width;
		private Integer height;
	}

}
