package br.com.e7.exploringmars.model;

import static br.com.e7.exploringmars.model.Direction.EAST;
import static br.com.e7.exploringmars.model.Direction.NORTH;
import static br.com.e7.exploringmars.model.Direction.SOUTH;
import static br.com.e7.exploringmars.model.Direction.WEST;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import br.com.e7.exploringmars.model.Rover.CoordinateValidation;
import br.com.e7.exploringmars.model.Rover.InvalidCoordinateException;

public class RoverTest {
	private static final CoordinateValidation v = (x,y) -> {if(!(x >= 0 && x <= 5 && y >= 0 && y <= 5)) throw new InvalidCoordinateException(String.format("cordinates: (%d, %d) are invalid", x, y));};
	
	@Test
	public void testRoverExample1() {
		Rover r = new Rover(1, 2, NORTH, v);
		r.turnLeft().move().turnLeft().move().turnLeft().move().turnLeft().move().move();
		assertThat(r.x()).isEqualTo(1);
		assertThat(r.y()).isEqualTo(3);
		assertThat(r.direction()).isEqualTo(NORTH);
	}
	
	@Test
	public void testRoverExample2() {
		Rover r = new Rover(3, 3, EAST, v);
		r.move().move().turnRight().move().move().turnRight().move().turnRight().turnRight().move();
		assertThat(r.x()).isEqualTo(5);
		assertThat(r.y()).isEqualTo(1);
		assertThat(r.direction()).isEqualTo(EAST);
	}
	
	@Test(expected=InvalidCoordinateException.class)
	public void testRoverNegativeCoordinateY() {
		Rover r = new Rover(0, 0, SOUTH, v);
		r.move();
	}
	
	@Test(expected=InvalidCoordinateException.class)
	public void testRoverNegativeCoordinateX() {
		Rover r = new Rover(0, 0, WEST, v);
		r.move();
	}
	
	@Test(expected=InvalidCoordinateException.class)
	public void testRoverOverflowCoordinateY() {
		Rover r = new Rover(5, 5, NORTH, v);
		r.move();
	}
	
	@Test(expected=InvalidCoordinateException.class)
	public void testRoverOverflowCoordinateX() {
		Rover r = new Rover(5, 5, EAST, v);
		r.move();
	}

}
