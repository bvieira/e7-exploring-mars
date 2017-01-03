package br.com.e7.exploringmars.model;

import static br.com.e7.exploringmars.model.Direction.EAST;
import static br.com.e7.exploringmars.model.Direction.NORTH;
import static br.com.e7.exploringmars.model.Direction.SOUTH;
import static br.com.e7.exploringmars.model.Direction.WEST;

public class Rover {
	public static interface CoordinateValidation{ void validate(int x, int y) throws InvalidCoordinateException; }
	
	private int x, y;
	private int directionIndex;
	private CoordinateValidation validation;

	public Rover(final int x, final int y, final Direction direction, final CoordinateValidation validation) {
		this.x = x;
		this.y = y;
		this.directionIndex = getIndexDirection(direction);
		this.validation = validation;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public Direction direction() {
		return directions[directionIndex];
	}
	
	public Rover move() throws InvalidCoordinateException {
		switch (direction()) {
		case NORTH:
			validation.validate(x, y+1);
			y++;
			break;
		case SOUTH:
			validation.validate(x, y-1);
			y--;
			break;
		case EAST:
			validation.validate(x+1, y);
			x++;
			break;
		case WEST:
			validation.validate(x-1, y);
			x--;
			break;
		}
		return this;
	}
	
	public Rover turnLeft() {
		directionIndex = directionIndex == 0 ? directions.length - 1 : directionIndex - 1;
		return this;
	}
	
	public Rover turnRight() {
		directionIndex = (directionIndex + 1) % directions.length;
		return this;
	}
	
	private static Direction[] directions = {NORTH, EAST, SOUTH, WEST};
	private static int getIndexDirection(final Direction d) {
		for(int i = 0; i < directions.length; i++)
			if(directions[i].equals(d))
				return i;
		throw new IllegalArgumentException("invalid direction");
	}
	
	public static class InvalidCoordinateException extends RuntimeException{ 
		private static final long serialVersionUID = -708453811796045932L; 
		
		public InvalidCoordinateException(final String message) {
			super(message);
		}
		
	}
}
