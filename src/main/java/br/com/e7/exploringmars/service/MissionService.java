package br.com.e7.exploringmars.service;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.MissionResult;

public interface MissionService {
	MissionResult process(Mission mission);
	
	Object search(String query, String sort);
}
