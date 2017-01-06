package br.com.e7.exploringmars.web;

import static br.com.e7.exploringmars.model.Action.LEFT;
import static br.com.e7.exploringmars.model.Action.MOVE;
import static br.com.e7.exploringmars.model.Action.RIGHT;
import static br.com.e7.exploringmars.util.ConfigProperties.DEFAULT_ENCODING;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Test;

import br.com.e7.exploringmars.model.Direction;
import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.MissionResult;
import br.com.e7.exploringmars.model.Rover;

public class MissionTextBodyReaderWriterTest {

	@Test
	public void testInputExample() {
		final String input = "5 5\n1 2 N\nLMLMLMLMM\n3 3 E\nMMRMMRMRRM";
		
		final RoverMission expectedRover1 = new RoverMission(new Rover(1, 2, Direction.NORTH, Rover.createSimpleCordinateValidation(5, 5)),
				Arrays.asList(LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, MOVE));
		final RoverMission expectedRover2 = new RoverMission(new Rover(3, 3, Direction.EAST, Rover.createSimpleCordinateValidation(5, 5)),
				Arrays.asList(MOVE, MOVE, RIGHT, MOVE, MOVE, RIGHT, MOVE, RIGHT, RIGHT, MOVE));
		
		final Mission result = new MissionTextBodyReader().parseMission("test", new ByteArrayInputStream(input.getBytes(Charset.forName(DEFAULT_ENCODING.value()))));
		
		assertThat(result.name()).isEqualTo("test");
		
		assertThat(result.surfaceWidth()).isEqualTo(5);
		assertThat(result.surfaceHeight()).isEqualTo(5);
		
		assertThat(result.rovers().get(0)).isEqualToComparingFieldByFieldRecursively(expectedRover1);
		assertThat(result.rovers().get(1)).isEqualToComparingFieldByFieldRecursively(expectedRover2);
	}
	
	@Test
	public void testOutputExample() {
		final MissionResult result = new MissionResult(Arrays.asList(new Rover(1, 3, Direction.NORTH, Rover.createSimpleCordinateValidation(5, 5)),
				new Rover(5, 1, Direction.EAST, Rover.createSimpleCordinateValidation(5, 5))));
		
		final String content = new String(new MissionResultTextBodyWriter().parseMissionResult(result));
		assertThat(content).isEqualTo("1 3 N\n5 1 E");
	}
	
}
