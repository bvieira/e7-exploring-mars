package br.com.e7.exploringmars.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import br.com.e7.exploringmars.model.Direction;
import br.com.e7.exploringmars.model.MissionResult;
import br.com.e7.exploringmars.model.Rover;
import br.com.e7.exploringmars.web.MissionResultBodyHandler.RoverOutput;

public class MissionResultBodyHandlerTest {
	
	@Test
	public void testOutputTextExample() {
		final MissionResult result = new MissionResult(Arrays.asList(new Rover(1, 3, Direction.NORTH),
				new Rover(5, 1, Direction.EAST)));
		
		final String content = new String(new MissionResultBodyHandler().toMissionOutputText(result));
		assertThat(content).isEqualTo("1 3 N\n5 1 E");
	}
	
	@Test
	public void testOutputJsonExample() {
		final MissionResult input = new MissionResult(Arrays.asList(new Rover(1, 3, Direction.NORTH),
				new Rover(5, 1, Direction.EAST)));
		
		final List<RoverOutput> result = new MissionResultBodyHandler().toMissionOutput(input);
		assertThat(result.get(0)).isEqualToComparingFieldByFieldRecursively(new RoverOutput(1, 3, Direction.NORTH));
		assertThat(result.get(1)).isEqualToComparingFieldByFieldRecursively(new RoverOutput(5, 1, Direction.EAST));
	}

}
