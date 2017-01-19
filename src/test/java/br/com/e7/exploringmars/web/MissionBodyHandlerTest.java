package br.com.e7.exploringmars.web;

import static br.com.e7.exploringmars.model.Action.LEFT;
import static br.com.e7.exploringmars.model.Action.MOVE;
import static br.com.e7.exploringmars.model.Action.RIGHT;
import static br.com.e7.exploringmars.util.ConfigProperties.DEFAULT_ENCODING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Test;

import br.com.e7.exploringmars.exception.InvalidCoordinateException;
import br.com.e7.exploringmars.exception.ParserException;
import br.com.e7.exploringmars.model.Direction;
import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.Rover;

public class MissionBodyHandlerTest {
	
	@Test
	public void testInputJsonExample() {
		final String input = "{\"surface\":{\"width\":5,\"height\":5},\"rovers\":[{\"x\":1,\"y\":2,\"direction\":\"NORTH\",\"actions\":[\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"MOVE\"]},{\"x\":3,\"y\":3,\"direction\":\"EAST\",\"actions\":[\"MOVE\",\"MOVE\",\"RIGHT\",\"MOVE\",\"MOVE\",\"RIGHT\",\"MOVE\",\"RIGHT\",\"RIGHT\",\"MOVE\"]}]}";
		
		final RoverMission expectedRover1 = new RoverMission(new Rover(1, 2, Direction.NORTH),
				Arrays.asList(LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, MOVE));
		final RoverMission expectedRover2 = new RoverMission(new Rover(3, 3, Direction.EAST),
				Arrays.asList(MOVE, MOVE, RIGHT, MOVE, MOVE, RIGHT, MOVE, RIGHT, RIGHT, MOVE));
		
		final Mission result = new MissionBodyHandler().parseMissionJson("test", new ByteArrayInputStream(input.getBytes(Charset.forName(DEFAULT_ENCODING.value()))));
		
		assertThat(result.name()).isEqualTo("test");
		
		assertThat(result.surfaceWidth()).isEqualTo(5);
		assertThat(result.surfaceHeight()).isEqualTo(5);
		
		assertThat(result.rovers().get(0)).isEqualToComparingFieldByFieldRecursively(expectedRover1);
		assertThat(result.rovers().get(1)).isEqualToComparingFieldByFieldRecursively(expectedRover2);
	}
	
	@Test
	public void testInputJsonRoverInTheSamePlace() {
		final String input = "{\"surface\":{\"width\":5,\"height\":5},\"rovers\":[{\"x\":1,\"y\":2,\"direction\":\"NORTH\",\"actions\":[\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"MOVE\"]},{\"x\":1,\"y\":2,\"direction\":\"EAST\",\"actions\":[\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"MOVE\"]}]}";
		
		assertThatThrownBy(() -> {  
			new MissionBodyHandler().parseMissionJson("test", new ByteArrayInputStream(input.getBytes(Charset.forName(DEFAULT_ENCODING.value())))); 
		}).isInstanceOf(InvalidCoordinateException.class).hasMessage("two rovers cannot occupy the same place, coordinate:(1,2) in use");
	}
	
	@Test
	public void testInputJsonParserError() {
		final String input = "{\"surface\":{\"width\":5,\"height\":5},\"rovers\":[{\"x\":1,\"y\":2,\"direction\":\"NORTH\",\"actions\":[\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"MOVE\"]},{\"x\":1,\"y\":2,\"direction\":\"EAST\",\"actions\":[\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"LEFT\",\"MOVE\",\"MOVE\"]}]";
		
		assertThatThrownBy(() -> {  
			new MissionBodyHandler().parseMissionJson("test", new ByteArrayInputStream(input.getBytes(Charset.forName(DEFAULT_ENCODING.value())))); 
		}).isInstanceOf(ParserException.class);
	}

	
	@Test
	public void testInputTextExample() {
		final String input = "5 5\n1 2 N\nLMLMLMLMM\n3 3 E\nMMRMMRMRRM";
		
		final RoverMission expectedRover1 = new RoverMission(new Rover(1, 2, Direction.NORTH),
				Arrays.asList(LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, MOVE));
		final RoverMission expectedRover2 = new RoverMission(new Rover(3, 3, Direction.EAST),
				Arrays.asList(MOVE, MOVE, RIGHT, MOVE, MOVE, RIGHT, MOVE, RIGHT, RIGHT, MOVE));
		
		final Mission result = new MissionBodyHandler().parseMissionText("test", new ByteArrayInputStream(input.getBytes(Charset.forName(DEFAULT_ENCODING.value()))));
		
		assertThat(result.name()).isEqualTo("test");
		
		assertThat(result.surfaceWidth()).isEqualTo(5);
		assertThat(result.surfaceHeight()).isEqualTo(5);
		
		assertThat(result.rovers().get(0)).isEqualToComparingFieldByFieldRecursively(expectedRover1);
		assertThat(result.rovers().get(1)).isEqualToComparingFieldByFieldRecursively(expectedRover2);
	}
	
	@Test
	public void testInputTextRoverInTheSamePlace() {
		final String input = "5 5\n1 2 N\nLMLMLMLMM\n1 2 E\nMMRMMRMRRM";
		
		assertThatThrownBy(() -> {  
			new MissionBodyHandler().parseMissionText("test", new ByteArrayInputStream(input.getBytes(Charset.forName(DEFAULT_ENCODING.value())))); 
		}).isInstanceOf(InvalidCoordinateException.class).hasMessage("two rovers cannot occupy the same place, coordinate:(1,2) in use");
	}
	
	@Test
	public void testInputTextParserError() {
		final String input = "5 5\n1 2 ";
		
		assertThatThrownBy(() -> {  
			new MissionBodyHandler().parseMissionText("test", new ByteArrayInputStream(input.getBytes(Charset.forName(DEFAULT_ENCODING.value())))); 
		}).isInstanceOf(ParserException.class).hasMessageContaining("invalid format");
	}


}
