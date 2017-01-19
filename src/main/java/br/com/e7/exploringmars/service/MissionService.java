package br.com.e7.exploringmars.service;

import java.util.HashSet;
import java.util.Set;

import br.com.e7.exploringmars.exception.InvalidCoordinateException;
import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.MissionResult;

public interface MissionService {
	MissionResult process(Mission mission);
	
	Object search(String query, String sort);
	
	public static interface CoordinateValidation{ 
		void validate(int x, int y) throws InvalidCoordinateException;
		void addInvalidPosition(int x, int y);
	}
	
	public static class SimpleCoordinateValidation implements CoordinateValidation {
		private final int width, height;
		private final Set<String> invalidCoordinates;
		
		public SimpleCoordinateValidation(final int width, final int height) {
			this.width = width;
			this.height = height;
			this.invalidCoordinates = new HashSet<>();
		}
		
		@Override
		public void validate(int x, int y) throws InvalidCoordinateException {
			if(!(x >= 0 && x <= width && y >= 0 && y <= height)) 
				throw new InvalidCoordinateException(String.format("coordinate: (%d, %d) is invalid", x, y));
			if(invalidCoordinates.contains(toCoordinateString(x, y)))
				throw new InvalidCoordinateException(String.format("coordinate: (%d, %d) is occupied", x, y));
		}
		
		@Override
		public void addInvalidPosition(int x, int y) {
			invalidCoordinates.add(toCoordinateString(x, y));
		}
		
		private String toCoordinateString(int x, int y) {
			return new StringBuilder().append(x).append(",").append(y).toString();
		}
	}
	
	static CoordinateValidation createSimpleCordinateValidation(final int w, final int h) {
		return new SimpleCoordinateValidation(w, h);
	}
}
