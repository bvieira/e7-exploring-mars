package br.com.e7.exploringmars.service.impl;

import static br.com.e7.exploringmars.model.Action.LEFT;
import static br.com.e7.exploringmars.model.Action.MOVE;
import static br.com.e7.exploringmars.model.Action.RIGHT;
import static br.com.e7.exploringmars.model.Direction.EAST;
import static br.com.e7.exploringmars.model.Direction.NORTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;

import br.com.e7.exploringmars.exception.InvalidCoordinateException;
import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.MissionResult;
import br.com.e7.exploringmars.model.Rover;
import br.com.e7.exploringmars.model.Rover.RoverPosition;
import br.com.e7.exploringmars.repository.MissionRepository;

public class MissionServiceImplTest {
	
	@Test
	public void testProcessExample1() {
		final Mission mission = new Mission("mission1", 5, 5);
		mission.addRoverMission(new RoverMission(new Rover(1, 2, NORTH),
				Arrays.asList(LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, MOVE)));
		mission.addRoverMission(new RoverMission(new Rover(3, 3, EAST),
				Arrays.asList(MOVE, MOVE, RIGHT, MOVE, MOVE, RIGHT, MOVE, RIGHT, RIGHT, MOVE)));
		
		final MissionRepository respository = mock(MissionRepository.class);
		final MissionResult result = new MissionServiceImpl(respository).process(mission);
		assertThat(result.rovers().get(0)).isEqualToComparingFieldByFieldRecursively(new Rover(new RoverPosition(1, 2, 0), new RoverPosition(1, 3, 0)));
		assertThat(result.rovers().get(1)).isEqualToComparingFieldByFieldRecursively(new Rover(new RoverPosition(3, 3, 1), new RoverPosition(5, 1, 1)));
		verify(respository, times(1)).add(mission);
	}
	
	@Test
	public void testProcessRoverOccupyingSameSpace() {
		final Mission mission = new Mission("mission1", 5, 5);
		mission.addRoverMission(new RoverMission(new Rover(1, 2, NORTH),
				Arrays.asList(LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, MOVE)));
		mission.addRoverMission(new RoverMission(new Rover(2, 2, EAST),
				Arrays.asList(LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, MOVE)));
		
		final MissionRepository respository = mock(MissionRepository.class);
		assertThatThrownBy(() -> { new MissionServiceImpl(respository).process(mission); }).isInstanceOf(InvalidCoordinateException.class).hasMessage("coordinate: (1, 3) is occupied");
		verify(respository, times(0)).add(mission);
	}
	
	@Test
	public void testProcessRoverCrashesOnAnotherRover() {
		final Mission mission = new Mission("mission1", 5, 5);
		mission.addRoverMission(new RoverMission(new Rover(1, 2, NORTH),
				Arrays.asList(LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, MOVE)));
		mission.addRoverMission(new RoverMission(new Rover(2, 2, EAST),
				Arrays.asList(LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, MOVE, MOVE)));
		
		final MissionRepository respository = mock(MissionRepository.class);
		assertThatThrownBy(() -> { new MissionServiceImpl(respository).process(mission); }).isInstanceOf(InvalidCoordinateException.class).hasMessage("coordinate: (1, 3) is occupied");
		verify(respository, times(0)).add(mission);
	}
	
	@Test
	public void testSearch() {
		final MissionRepository respository = mock(MissionRepository.class);
		new MissionServiceImpl(respository).search("*:*", "id:asc");
		verify(respository, times(1)).search("*:*", "id:asc");
	}

}
