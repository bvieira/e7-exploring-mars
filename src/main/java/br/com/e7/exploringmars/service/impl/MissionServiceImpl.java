package br.com.e7.exploringmars.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.Rover;
import br.com.e7.exploringmars.service.MissionService;

public class MissionServiceImpl implements MissionService {

	@Override
	public List<Rover> process(final Mission mission) {
		return mission.rovers().stream().map(m -> processRoverMission(m)).collect(Collectors.toList());
	}
	
	private Rover processRoverMission(final RoverMission roverMission) {
		roverMission.actions().forEach(a -> {
			switch (a) {
			case LEFT:
				roverMission.rover().turnLeft();
				break;
			case RIGHT:
				roverMission.rover().turnRight();
				break;
			case MOVE:
				roverMission.rover().move();
				break;
			}
		});
		return roverMission.rover();
	}
}
