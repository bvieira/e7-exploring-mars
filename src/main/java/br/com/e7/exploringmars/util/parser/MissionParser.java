package br.com.e7.exploringmars.util.parser;

import java.util.List;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Rover;

public interface MissionParser {
	
	Mission parseMission(String name, String content);
	
	byte[] parseMissionResult(List<Rover> rover);
	
	public static class ParserException extends RuntimeException {
		private static final long serialVersionUID = 7723934719837541791L;

		public ParserException(final String msg) {
			super(msg);
		}
	}
}
