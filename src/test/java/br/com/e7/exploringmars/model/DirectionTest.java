package br.com.e7.exploringmars.model;

import static org.assertj.core.api.Assertions.assertThat;
import static br.com.e7.exploringmars.model.Direction.*;
import org.junit.Test;

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
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetDirectionInvalid() {
		Direction.get('A');
	}

}
