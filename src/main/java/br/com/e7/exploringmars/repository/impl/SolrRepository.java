package br.com.e7.exploringmars.repository.impl;

import static br.com.e7.exploringmars.util.ConfigProperties.SOLR_URL;

import java.io.IOException;

import javax.inject.Singleton;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

@Singleton
public class SolrRepository {
	private final SolrClient client;
	
	public SolrRepository() {
		client = new HttpSolrClient.Builder().withBaseSolrUrl(SOLR_URL.value()).build();
	}
	
	public void add(final Object document) {
		try {
			client.addBean(document);
			client.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		
	}
}
