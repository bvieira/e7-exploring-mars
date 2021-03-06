package br.com.e7.exploringmars.util;

import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;

import br.com.e7.exploringmars.repository.MissionRepository;
import br.com.e7.exploringmars.repository.impl.SolrMissionRepository;
import br.com.e7.exploringmars.repository.impl.SolrRepository;
import br.com.e7.exploringmars.service.MissionService;
import br.com.e7.exploringmars.service.impl.MissionServiceImpl;
import br.com.e7.exploringmars.web.ApplicationExceptionMapper;
import br.com.e7.exploringmars.web.ExploringMissionREST;
import br.com.e7.exploringmars.web.JsonMessageBodyHandler;
import br.com.e7.exploringmars.web.MissionBodyHandler;
import br.com.e7.exploringmars.web.MissionResultBodyHandler;
import br.com.e7.exploringmars.web.WebApplicationExceptionMapper;

public class GuiceModule implements Module {
	
	public GuiceModule() {
		LoggerFactory.getLogger("init").info("starting guice");
	}

	@Override
	public void configure(Binder binder) {
		binder.install(new DependencyInjector());
		
		binder.bind(MissionBodyHandler.class);
		binder.bind(MissionResultBodyHandler.class);
		binder.bind(JsonMessageBodyHandler.class);
		binder.bind(ApplicationExceptionMapper.class);
		binder.bind(WebApplicationExceptionMapper.class);
		binder.bind(ExploringMissionREST.class);
	}
	
	
	public static class DependencyInjector extends AbstractModule {

		@Override
		protected void configure() {
			bind(SolrRepository.class);
			bind(MissionRepository.class).to(SolrMissionRepository.class);
			bind(MissionService.class).to(MissionServiceImpl.class);
		}
		
	}

}
