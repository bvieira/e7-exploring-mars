package br.com.e7.exploringmars.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.params.CommonParams;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import br.com.e7.exploringmars.exception.ParserException;
import br.com.e7.exploringmars.exception.RepositoryIndexingException;
import br.com.e7.exploringmars.exception.RepositorySearchingException;
import br.com.e7.exploringmars.repository.MissionRepository.MissionRepositoryInfo;


public class SolrRepositoryTest {

	@Test
	public void testAdd() throws Exception {
		final SolrClient client = mock(SolrClient.class);
		final MissionRepositoryInfo input = new MissionRepositoryInfo();
		
		new SolrRepository(client).add(input);
		
		verify(client, times(1)).addBean(input);
		verify(client, times(1)).commit();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAddError() throws Exception {
		final MissionRepositoryInfo input = new MissionRepositoryInfo();
		final SolrClient client = mock(SolrClient.class);
		when(client.addBean(input)).thenThrow(SolrServerException.class);
		
		assertThatThrownBy(() -> { new SolrRepository(client).add(input); }).isInstanceOf(RepositoryIndexingException.class).hasMessageContaining("error adding document on solr");
		verify(client, times(0)).commit();
	}
	
	@Test
	public void testQuery() throws Exception {
		final SolrClient client = mock(SolrClient.class);
		
		new SolrRepository(client).query("*:*", "id:asc");
		
		ArgumentCaptor<SolrQuery> argument = ArgumentCaptor.forClass(SolrQuery.class);
		verify(client, times(1)).query(argument.capture());
		assertThat(argument.getValue().get(CommonParams.Q)).isEqualTo("*:*");
		assertThat(argument.getValue().getSorts().get(0).getItem()).isEqualTo("id");
		assertThat(argument.getValue().getSorts().get(0).getOrder()).isEqualTo(ORDER.asc);
	}
	
	@Test
	public void testQueryInvalidSort() throws Exception {
		final SolrClient client = mock(SolrClient.class);
		
		assertThatThrownBy(() -> { new SolrRepository(client).query("*:*", "idasc"); }).isInstanceOf(ParserException.class).hasMessage("sort param is invalid, value: idasc");
		
		verify(client, times(0)).query(any());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testQueryError() throws Exception {
		final SolrClient client = mock(SolrClient.class);
		when(client.query(any())).thenThrow(SolrServerException.class);
		
		assertThatThrownBy(() -> { new SolrRepository(client).query("*:*", "id:asc"); }).isInstanceOf(RepositorySearchingException.class).hasMessageContaining("error searching document on solr");

		verify(client, times(1)).query(any());
	}
}
