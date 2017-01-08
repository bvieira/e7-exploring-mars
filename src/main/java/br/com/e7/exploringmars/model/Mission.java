package br.com.e7.exploringmars.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Mission {
	private final String name;
	private final long created;
	private final List<RoverMission> rovers;
	private final int surfaceWidth, surfaceHeight;
	
	public Mission(final String name, final int surfaceWidth, final int surfaceHeight) {
		this.name = name;
		this.surfaceWidth = surfaceWidth;
		this.surfaceHeight = surfaceHeight;
		this.created = Instant.now().toEpochMilli();
		this.rovers = new ArrayList<>();
	}
	
	public String name() {
		return name;
	}
	
	public long created() {
		return created;
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
	
	public int surfaceWidth() {
		return surfaceWidth;
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
