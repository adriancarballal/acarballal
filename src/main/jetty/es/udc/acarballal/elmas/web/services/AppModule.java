package es.udc.acarballal.elmas.web.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.services.Dispatcher;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {

	public static void bind(ServiceBinder binder) {

		/* Bind dispatchers. */
		binder.bind(SessionDispatcher.class).withId("SessionDispatcher");
		binder.bind(AuthenticationDispatcher.class).
			withId("AuthenticationDispatcher");

	}

	public static void contributeApplicationDefaults(
			MappedConfiguration<String, String> configuration) {
		
        // Contributions to ApplicationDefaults will override any contributions
    	// to FactoryDefaults (with the same key). Here we're restricting the 
    	// supported locales.
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "gl,es,en");

        // The factory default is true but during the early stages of an 
        // application overriding to false is a good idea. In addition, this is
        // often overridden on the command line as 
        // -Dtapestry.production-mode=false
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
		
	}

	public void contributeMasterDispatcher(
		OrderedConfiguration<Dispatcher> configuration,
		@InjectService("SessionDispatcher")
		Dispatcher sessionDispatcher,
		@InjectService("AuthenticationDispatcher")
		Dispatcher authenticationDispatcher) {

		/* Add dispatchers to the master Dispatcher service. */
		configuration.add("SessionDispatcher", sessionDispatcher,
			"before:AuthenticationDispatcher");
		configuration.add("AuthenticationDispatcher", authenticationDispatcher,
			"before:PageRender");
		
	}

}
