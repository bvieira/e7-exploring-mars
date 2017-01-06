package br.com.e7.exploringmars.service.impl;

import java.util.stream.Collectors;

import javax.inject.Singleton;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.MissionResult;
import br.com.e7.exploringmars.model.Rover;
import br.com.e7.exploringmars.service.MissionService;

@Singleton
public class MissionServiceImpl implements MissionService {

	@Override
	public MissionResult process(final Mission mission) {
		return new MissionResult(mission.rovers().stream().map(m -> processRoverMission(m)).collect(Collectors.toList()));
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
