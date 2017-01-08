package br.com.e7.exploringmars.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.MissionResult;
import br.com.e7.exploringmars.model.Rover;
import br.com.e7.exploringmars.repository.MissionRepository;
import br.com.e7.exploringmars.repository.MissionRepository.MissionRepositoryInfo;
import br.com.e7.exploringmars.service.MissionService;

@Singleton
public class MissionServiceImpl implements MissionService {
	
	private final MissionRepository repository;
	
	@Inject
	public MissionServiceImpl(final MissionRepository repository) {
		this.repository = repository;
	}

	@Override
	public MissionResult process(final Mission mission) {
		final CoordinateValidation v = MissionService.createSimpleCordinateValidation(mission.surfaceWidth(), mission.surfaceHeight());
		final MissionResult result = new MissionResult(mission.rovers().stream().map(m -> processRoverMission(m, v)).collect(Collectors.toList()));
		repository.add(mission);
		return result;
	}
	
	@Override
	public List<MissionRepositoryInfo> search(final String query, final String sort) {
		return repository.search(query, sort);
	}
	
	private Rover processRoverMission(final RoverMission roverMission, final CoordinateValidation v) {
		roverMission.actions().forEach(a -> {
			switch (a) {
			case LEFT:
				roverMission.rover().turnLeft();
				break;
			case RIGHT:
				roverMission.rover().turnRight();
				break;
			case MOVE:
				roverMission.rover().move(v);
				break;
			}
		});
		v.addInvalidPosition(roverMission.rover().x(), roverMission.rover().y());
		return roverMission.rover();
	}
}
