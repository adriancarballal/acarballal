package es.udc.acarballal.elmas.web.components;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import es.udc.acarballal.elmas.web.util.UserSession;

public class MobileLayout {

	@SuppressWarnings("unused")
	@Property
	@Parameter(required = true, defaultPrefix = "literal")
	private String pageTitle;

	@SuppressWarnings("unused")
	@Property
	@ApplicationState
	private UserSession userSession;

	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;

}
