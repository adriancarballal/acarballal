package es.udc.acarballal.elmas.web.pages.errors;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;

import es.udc.acarballal.elmas.web.util.UserSession;

public class InsufficientPrivileges {

	@SuppressWarnings("unused")
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
}
