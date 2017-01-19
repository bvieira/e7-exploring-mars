package br.com.e7.exploringmars.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.solr.client.solrj.response.QueryResponse;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.repository.MissionRepository;

@Singleton
public class SolrMissionRepository implements MissionRepository {

	private final SolrRepository solr;
	
	@Inject
	public SolrMissionRepository(final SolrRepository solr) {
		this.solr = solr;
	}
	
	@Override
	public void add(final Mission mission) {
		solr.add(new MissionRepositoryInfo(mission.name(), mission.name(), new Date(mission.created()), 
				mission.surfaceWidth(), mission.surfaceHeight(), 
				mission.rovers().size(),
				mission.rovers().stream().map(r -> new StringBuilder().append(r.rover().initialX()).append(",").append(r.rover().initialY()).toString()).collect(Collectors.toList()),
				mission.rovers().stream().map(r -> new StringBuilder().append(r.rover().x()).append(",").append(r.rover().y()).toString()).collect(Collectors.toList())
		));
	}
	
	@Override
	public List<MissionRepositoryInfo> search(final String query, final String sort) {
		final QueryResponse response = solr.query(query, sort);
		return response.getBeans(MissionRepositoryInfo.class);
	}
	
}
