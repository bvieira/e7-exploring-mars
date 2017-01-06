package br.com.e7.exploringmars.model;

import java.util.ArrayList;
import java.util.List;

public class Mission {
	private final String name;
	private final List<RoverMission> rovers;
	private int surfaceWidth, surfaceHeight;
	
	public Mission(final String name) {
		this.name = name;
		this.rovers = new ArrayList<>();
	}
	
	public String name() {
		return name;
	}
	
	public List<RoverMission> rovers() {
		return rovers;
	}
	
	public void addRoverMission(final RoverMission roverMission) {
		rovers.add(roverMission);
	}
	
	public int surfaceHeight() {
		return surfaceHeight;
	}
	
	public void setSurfaceHeight(final int surfaceHeight) {
		this.surfaceHeight = surfaceHeight;
	}
	
	public int surfaceWidth() {
		return surfaceWidth;
	}
	
	public void setSurfaceWidth(final int surfaceWidth) {
		this.surfaceWidth = surfaceWidth;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Mission [name=").append(name).append(", rovers=").append(rovers).append(", surfaceWidth=").append(surfaceWidth).append(", surfaceHeight=").append(surfaceHeight).append("]").toString();
	}

	public static class RoverMission {
		private final Rover rover;
		private final List<Action> actions;
		
		public RoverMission(final Rover rover, final List<Action> actions) {
			this.rover = rover;
			this.actions = actions;
		}
		
		public Rover rover() {
			return rover;
		}
		
		public List<Action> actions() {
			return actions;
		}

		@Override
		public String toString() {
			return new StringBuilder("RoverMission [rover=").append(rover).append(", actions=").append(actions).append("]").toString();
		}
		
		
	}
}
