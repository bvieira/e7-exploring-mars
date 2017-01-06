package br.com.e7.exploringmars.model;

import java.util.List;

public class MissionResult {
	private final List<Rover> rovers;
	
	public MissionResult(final List<Rover> rovers) {
		this.rovers = rovers;
	}
	
	public List<Rover> rovers() {
		return rovers;
	}
}
