package br.com.e7.exploringmars.repository;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.MissionResult;

public interface MissionRepository {
	void add(Mission mission, MissionResult result);

}
