package br.com.e7.exploringmars.repository.impl;

import static br.com.e7.exploringmars.model.Action.LEFT;
import static br.com.e7.exploringmars.model.Action.MOVE;
import static br.com.e7.exploringmars.model.Action.RIGHT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.Mission.RoverMission;
import br.com.e7.exploringmars.model.Rover;
import br.com.e7.exploringmars.model.Rover.RoverPosition;
import br.com.e7.exploringmars.repository.MissionRepository.MissionRepositoryInfo;

public class SolrMissionRepositoryTest {
	
	@Test
	public void testAdd() {
		final Mission mission = new Mission("mission1");
		mission.addRoverMission(new RoverMission(new Rover(new RoverPosition(1, 2, 0), new RoverPosition(1, 3, 0), Rover.createSimpleCordinateValidation(5, 5)),
				Arrays.asList(LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, LEFT, MOVE, MOVE)));
		mission.addRoverMission(new RoverMission(new Rover(new RoverPosition(3, 3, 1), new RoverPosition(5, 1, 1), Rover.createSimpleCordinateValidation(5, 5)),
				Arrays.asList(MOVE, MOVE, RIGHT, MOVE, MOVE, RIGHT, MOVE, RIGHT, RIGHT, MOVE)));
		
		final SolrRepository respository = mock(SolrRepository.class);

		new SolrMissionRepository(respository).add(mission);

		ArgumentCaptor<MissionRepositoryInfo> argument = ArgumentCaptor.forClass(MissionRepositoryInfo.class);
		verify(respository, times(1)).add(argument.capture());
		final MissionRepositoryInfo expected = new MissionRepositoryInfo(mission.name(), mission.name(), new Date(mission.created()), mission.surfaceWidth(), mission.surfaceHeight(), mission.rovers().size(), Arrays.asList("1,2","3,3"), Arrays.asList("1,3","5,1"));
		assertThat(argument.getValue()).isEqualToComparingFieldByFieldRecursively(expected);
	}
	
	@Test
	public void testSearch() {
		final QueryResponse queryResponse = mock(QueryResponse.class);
		final SolrRepository respository = mock(SolrRepository.class);
		when(respository.query(any(), any())).thenReturn(queryResponse);
		when(queryResponse.getBeans(any())).thenReturn(Collections.emptyList());
		
		new SolrMissionRepository(respository).search("*:*", "id:asc");
		verify(respository, times(1)).query("*:*", "id:asc");
		verify(queryResponse, times(1)).getBeans(any());
	}

}
