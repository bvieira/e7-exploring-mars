package br.com.e7.exploringmars.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static br.com.e7.exploringmars.model.Direction.*;
import org.junit.Test;

import br.com.e7.exploringmars.model.Rover.InvalidCoordinateException;

public class DirectionTest {
	
	@Test
	public void testValueEast() {
		assertThat(EAST.value()).isEqualTo('E');
	}
	
	@Test
	public void testValueNorth() {
		assertThat(NORTH.value()).isEqualTo('N');
	}
	
	@Test
	public void testGetDirection() {
		assertThat(Direction.get('N')).isEqualTo(NORTH);
	}
	
	@Test
	public void testGetDirectionLowercase() {
		assertThat(Direction.get('w')).isEqualTo(WEST);
	}
	
	@Test
	public void testGetDirectionInvalid() {
		assertThatThrownBy(() -> { Direction.get('A'); }).isInstanceOf(IllegalArgumentException.class).hasMessage("invalid direction");
	}

}
