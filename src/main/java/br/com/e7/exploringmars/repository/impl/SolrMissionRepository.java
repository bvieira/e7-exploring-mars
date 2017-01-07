package br.com.e7.exploringmars.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.solr.client.solrj.beans.Field;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.MissionResult;
import br.com.e7.exploringmars.repository.MissionRepository;

@Singleton
public class SolrMissionRepository implements MissionRepository {

	private final SolrRepository solr;
	
	@Inject
	public SolrMissionRepository(final SolrRepository solr) {
		this.solr = solr;
	}
	
	@Override
	public void add(final Mission mission, final MissionResult result) {
		solr.add(new SolrMission(mission.name(), mission.name(), new Date(mission.created()), 
				mission.surfaceWidth(), mission.surfaceHeight(), 
				mission.rovers().size(),
				mission.rovers().stream().map(r -> r.rover().initialX() + "," + r.rover().initialY()).collect(Collectors.toList()),
				mission.rovers().stream().map(r -> r.rover().x() + "," + r.rover().y()).collect(Collectors.toList())
		));
	}
	
	private static class SolrMission {
		
		@Field("id")
		String id;
		
		@Field("name")
		String name;
		
		@Field("created")
		Date created;
		
		@Field("width")
		int width;
		
		@Field("height")
		int height;
		
		@Field("qty_rover")
		int qtyRover;
		
		@Field("start_points")
		List<String> startPoints;
		
		@Field("end_points")
		List<String> endPoints;
		
		public SolrMission(final String id, final String name, final Date created, final int width, final int height, final int qtyRover, final List<String> startPoints, final List<String> endPoints) {
			this.id = id;
			this.name = name;
			this.created = created;
			this.width = width;
			this.height = height;
			this.qtyRover = qtyRover;
			this.startPoints = startPoints;
			this.endPoints = endPoints;
		}
	}
	
}
