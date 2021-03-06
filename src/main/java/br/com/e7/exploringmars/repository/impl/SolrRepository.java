package br.com.e7.exploringmars.repository.impl;

import static br.com.e7.exploringmars.util.ConfigProperties.SOLR_URL;

import java.io.IOException;

import javax.inject.Singleton;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.response.QueryResponse;

import br.com.e7.exploringmars.exception.ParserException;
import br.com.e7.exploringmars.exception.RepositoryIndexingException;
import br.com.e7.exploringmars.exception.RepositorySearchingException;

@Singleton
public class SolrRepository {
	private final SolrClient client;
	
	public SolrRepository() {
		this(new HttpSolrClient.Builder().withBaseSolrUrl(SOLR_URL.value()).build());
	}
	
	SolrRepository(final SolrClient client) {
		this.client = client;
	}
	
	public void add(final Object document) {
		try {
			client.addBean(document);
			client.commit();
		} catch (SolrServerException | RemoteSolrException | IOException e) {
			throw new RepositoryIndexingException(new StringBuilder("error adding document on solr, cause: ").append(e.getMessage()).toString(), e);
		}
	}
	
	public QueryResponse query(final String q, final String sort) {
		try {
			final SolrQuery query = new SolrQuery(q);
			if(sort != null && !sort.trim().isEmpty()) {
				final String[] params = getSortParams(sort);
				query.addSort(new SortClause(params[0], params[1]));
			}
			return client.query(query);
		} catch (SolrServerException | RemoteSolrException | IOException e) {
			throw new RepositorySearchingException(new StringBuilder("error searching document on solr, cause: ").append(e.getMessage()).toString(), e);
		}
	}
	
	private String[] getSortParams(final String content) {
		final String[] params = content.split(":");
		if(params.length != 2)
			throw new ParserException(new StringBuilder("sort param is invalid, value: ").append(content).toString());
		return params;
	}
}
