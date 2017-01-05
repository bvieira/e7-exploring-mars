package br.com.e7.exploringmars.util.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import br.com.e7.exploringmars.model.Action;
import br.com.e7.exploringmars.model.Direction;
import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.Rover;
import br.com.e7.exploringmars.model.Rover.CoordinateValidation;

public class TextParser implements MissionParser {

	@Override
	public Mission parse(String name, String content) {
		final Mission mission = new Mission(name);
		try(BufferedReader bufReader = new BufferedReader(new StringReader(content))) {
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
	
	@Override
	public byte[] parse(Mission mission) {
		return mission.rovers().stream().map(r -> r.rover().x() + " " + r.rover().y() + " " + r.rover().direction().value()).collect(Collectors.joining("\n")).getBytes();
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
