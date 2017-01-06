package br.com.e7.exploringmars.model;

import java.util.Arrays;

import br.com.e7.exploringmars.exception.InvalidDirectionException;

public enum Direction {
	NORTH, EAST, SOUTH, WEST;
	
	public char value() {
		return name().charAt(0);
	}
	
	public static Direction get(final char value) {
		return Arrays.stream(Direction.values()).filter(r -> r.value() == Character.toUpperCase(value)).findFirst().orElseThrow(() -> new InvalidDirectionException("invalid direction"));
	}
}
