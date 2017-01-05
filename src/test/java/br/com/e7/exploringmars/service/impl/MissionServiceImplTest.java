package br.com.e7.exploringmars.service.impl;

import static br.com.e7.exploringmars.model.Action.LEFT;
import static br.com.e7.exploringmars.model.Action.MOVE;
import static br.com.e7.exploringmars.model.Action.RIGHT;
import static br.com.e7.exploringmars.model.Direction.EAST;
import static br.com.e7.exploringmars.model.Direction.NORTH;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.Rover;

public class MissionServiceImplTest {
	
	@Test
	public void testMissionServiceExample1() {
		final Mission mission = new Mission("mission1");
		mission.addRoverMission(new RoverMission(new Rover(1, 2, NORTH, Rover.createSimpleCordinateValidation(5, 5)),
				Arrays.asList(LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, MOVE)));
		mission.addRoverMission(new RoverMission(new Rover(3, 3, EAST, Rover.createSimpleCordinateValidation(5, 5)),
				Arrays.asList(MOVE, MOVE, RIGHT, MOVE, MOVE, RIGHT, MOVE, RIGHT, RIGHT, MOVE)));
		
		final List<Rover> result = new MissionServiceImpl().process(mission);
		assertThat(result.get(0)).isEqualToComparingFieldByFieldRecursively(new Rover(1, 3, NORTH, Rover.createSimpleCordinateValidation(5, 5)));
		assertThat(result.get(1)).isEqualToComparingFieldByFieldRecursively(new Rover(5, 1, EAST, Rover.createSimpleCordinateValidation(5, 5)));
	}

}
