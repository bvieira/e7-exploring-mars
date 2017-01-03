package br.com.e7.exploringmars.model;

import java.util.Arrays;

public enum Direction {
	NORTH, EAST, SOUTH, WEST;
	
	public char value() {
		return name().charAt(0);
	}
	
	public static Direction get(final char value) {
		return Arrays.stream(Direction.values()).filter(r -> r.value() == Character.toUpperCase(value)).findFirst().orElseThrow(() -> new IllegalArgumentException("invalid direction"));
	}
}
