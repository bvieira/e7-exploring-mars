package br.com.e7.exploringmars.model;

import static br.com.e7.exploringmars.model.Direction.EAST;
import static br.com.e7.exploringmars.model.Direction.NORTH;
import static br.com.e7.exploringmars.model.Direction.SOUTH;
import static br.com.e7.exploringmars.model.Direction.WEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import br.com.e7.exploringmars.exception.InvalidCoordinateException;
import br.com.e7.exploringmars.model.Rover.CoordinateValidation;

public class RoverTest {
	private static final CoordinateValidation v = Rover.createSimpleCordinateValidation(5, 5);
	
	@Test
	public void testRoverExample1() {
		final Rover r = new Rover(1, 2, NORTH, v);
		r.turnLeft().move().turnLeft().move().turnLeft().move().turnLeft().move().move();
		assertThat(r.x()).isEqualTo(1);
		assertThat(r.y()).isEqualTo(3);
		assertThat(r.direction()).isEqualTo(NORTH);
	}
	
	@Test
	public void testRoverExample2() {
		final Rover r = new Rover(3, 3, EAST, v);
		r.move().move().turnRight().move().move().turnRight().move().turnRight().turnRight().move();
		assertThat(r.x()).isEqualTo(5);
		assertThat(r.y()).isEqualTo(1);
		assertThat(r.direction()).isEqualTo(EAST);
	}
	
	@Test
	public void testRoverNegativeCoordinateY() {
		final Rover r = new Rover(0, 0, SOUTH, v);
		assertThatThrownBy(r::move).isInstanceOf(InvalidCoordinateException.class).hasMessage("cordinates: (0, -1) are invalid");
	}
	
	@Test
	public void testRoverNegativeCoordinateX() {
		final Rover r = new Rover(0, 0, WEST, v);
		assertThatThrownBy(r::move).isInstanceOf(InvalidCoordinateException.class).hasMessage("cordinates: (-1, 0) are invalid");
	}
	
	@Test
	public void testRoverOverflowCoordinateY() {
		final Rover r = new Rover(5, 5, NORTH, v);
		assertThatThrownBy(r::move).isInstanceOf(InvalidCoordinateException.class).hasMessage("cordinates: (5, 6) are invalid");
	}
	
	@Test
	public void testRoverOverflowCoordinateX() {
		final Rover r = new Rover(5, 5, EAST, v);
		assertThatThrownBy(r::move).isInstanceOf(InvalidCoordinateException.class).hasMessage("cordinates: (6, 5) are invalid");
	}

}
