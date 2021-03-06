package br.com.e7.exploringmars.model;

import static br.com.e7.exploringmars.model.Direction.EAST;
import static br.com.e7.exploringmars.model.Direction.NORTH;
import static br.com.e7.exploringmars.model.Direction.SOUTH;
import static br.com.e7.exploringmars.model.Direction.WEST;

import br.com.e7.exploringmars.exception.InvalidCoordinateException;
import br.com.e7.exploringmars.exception.InvalidDirectionException;
import br.com.e7.exploringmars.service.MissionService.CoordinateValidation;

public class Rover {
	
	
	private final RoverPosition initialPosition;
	private final RoverPosition currentPosition;

	public Rover(final int x, final int y, final Direction direction) {
		this(new RoverPosition(x, y, getIndexDirection(direction)), new RoverPosition(x, y, getIndexDirection(direction)));
	}
	
	public Rover(final RoverPosition initialPosition, final RoverPosition currentPosition) {
		this.initialPosition = initialPosition;
		this.currentPosition = currentPosition;
	}
	
	public int initialX() {
		return initialPosition.x;
	}
	
	public int initialY() {
		return initialPosition.y;
	}
	
	public Direction initialDirection() {
		return directions[initialPosition.directionIndex];
	}
	
	public int x() {
		return currentPosition.x;
	}
	
	public int y() {
		return currentPosition.y;
	}
	
	public Direction direction() {
		return directions[currentPosition.directionIndex];
	}
	
	public Rover move(final CoordinateValidation validation) throws InvalidCoordinateException {
		switch (direction()) {
		case NORTH:
			validation.validate(x(), y()+1);
			currentPosition.y++;
			break;
		case SOUTH:
			validation.validate(x(), y()-1);
			currentPosition.y--;
			break;
		case EAST:
			validation.validate(x()+1, y());
			currentPosition.x++;
			break;
		case WEST:
			validation.validate(x()-1, y());
			currentPosition.x--;
			break;
		}
		return this;
	}
	
	public Rover turnLeft() {
		currentPosition.directionIndex = currentPosition.directionIndex == 0 ? directions.length - 1 : currentPosition.directionIndex - 1;
		return this;
	}
	
	public Rover turnRight() {
		currentPosition.directionIndex = (currentPosition.directionIndex + 1) % directions.length;
		return this;
	}
	
	private static Direction[] directions = {NORTH, EAST, SOUTH, WEST};
	private static int getIndexDirection(final Direction d) {
		for(int i = 0; i < directions.length; i++)
			if(directions[i].equals(d))
				return i;
		throw new InvalidDirectionException("invalid direction");
	}
	
	public static class RoverPosition {
		private int x, y;
		private int directionIndex;
		
		public RoverPosition(final int x, final int y, final int directionIndex) {
			this.x = x;
			this.y = y;
			this.directionIndex = directionIndex;
		}
	}
}
