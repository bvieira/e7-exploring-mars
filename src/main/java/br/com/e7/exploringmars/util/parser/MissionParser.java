package br.com.e7.exploringmars.util.parser;

import br.com.e7.exploringmars.model.Mission;

public interface MissionParser {
	Mission parse(String name, String content);
	byte[] parse(Mission mission);
	
	public static class ParserException extends RuntimeException {
		private static final long serialVersionUID = 7723934719837541791L;

		public ParserException(final String msg) {
			super(msg);
		}
	}
}
