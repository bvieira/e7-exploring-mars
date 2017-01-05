package br.com.e7.exploringmars.util;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;

import br.com.e7.exploringmars.service.MissionService;
import br.com.e7.exploringmars.service.impl.MissionServiceImpl;
import br.com.e7.exploringmars.web.ExploringMissionREST;

public class GuiceModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.install(new DependencyInjector());
		binder.bind(ExploringMissionREST.class);
	}
	
	
	public static class DependencyInjector extends AbstractModule {

		@Override
		protected void configure() {
			bind(MissionService.class).to(MissionServiceImpl.class);
		}
		
	}

}
