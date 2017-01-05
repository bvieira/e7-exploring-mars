package br.com.e7.exploringmars.model;

import java.util.Arrays;

public enum Action {
	LEFT, RIGHT, MOVE;

	public char value() {
		return name().charAt(0);
	}

	public static Action get(final char value) {
		return Arrays.stream(Action.values()).filter(r -> r.value() == Character.toUpperCase(value)).findFirst().orElseThrow(() -> new IllegalArgumentException("invalid action"));
	}
}