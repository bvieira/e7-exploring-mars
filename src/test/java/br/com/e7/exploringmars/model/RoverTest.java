package br.com.e7.exploringmars.model;

import static br.com.e7.exploringmars.model.Direction.EAST;
import static br.com.e7.exploringmars.model.Direction.NORTH;
import static br.com.e7.exploringmars.model.Direction.SOUTH;
import static br.com.e7.exploringmars.model.Direction.WEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import br.com.e7.exploringmars.exception.InvalidCoordinateException;
import br.com.e7.exploringmars.service.MissionService;
import br.com.e7.exploringmars.service.MissionService.CoordinateValidation;

public class RoverTest {
	private static final CoordinateValidation v = MissionService.createSimpleCordinateValidation(5, 5);
	
	@Test
	public void testRoverExample1() {
		final Rover r = new Rover(1, 2, NORTH);
		r.turnLeft().move(v).turnLeft().move(v).turnLeft().move(v).turnLeft().move(v).move(v);
		assertThat(r.x()).isEqualTo(1);
		assertThat(r.y()).isEqualTo(3);
		assertThat(r.direction()).isEqualTo(NORTH);
	}
	
	@Test
	public void testRoverExample2() {
		final Rover r = new Rover(3, 3, EAST);
		r.move(v).move(v).turnRight().move(v).move(v).turnRight().move(v).turnRight().turnRight().move(v);
		assertThat(r.x()).isEqualTo(5);
		assertThat(r.y()).isEqualTo(1);
		assertThat(r.direction()).isEqualTo(EAST);
	}
	
	@Test
	public void testRoverNegativeCoordinateY() {
		final Rover r = new Rover(0, 0, SOUTH);
		assertThatThrownBy(() -> { r.move(v); }).isInstanceOf(InvalidCoordinateException.class).hasMessage("coordinate: (0, -1) is invalid");
	}
	
	@Test
	public void testRoverNegativeCoordinateX() {
		final Rover r = new Rover(0, 0, WEST);
		assertThatThrownBy(() -> { r.move(v); }).isInstanceOf(InvalidCoordinateException.class).hasMessage("coordinate: (-1, 0) is invalid");
	}
	
	@Test
	public void testRoverOverflowCoordinateY() {
		final Rover r = new Rover(5, 5, NORTH);
		assertThatThrownBy(() -> { r.move(v); }).isInstanceOf(InvalidCoordinateException.class).hasMessage("coordinate: (5, 6) is invalid");
	}
	
	@Test
	public void testRoverOverflowCoordinateX() {
		final Rover r = new Rover(5, 5, EAST);
		assertThatThrownBy(() -> { r.move(v); }).isInstanceOf(InvalidCoordinateException.class).hasMessage("coordinate: (6, 5) is invalid");
	}

}
