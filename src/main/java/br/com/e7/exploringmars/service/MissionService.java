package br.com.e7.exploringmars.service;

import java.util.List;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Rover;

public interface MissionService {
	List<Rover> process(Mission mission);
}
