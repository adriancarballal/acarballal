package es.udc.acarballal.elmas.web.pages.user;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.util.CookiesManager;
import es.udc.acarballal.elmas.web.util.PageSession;
import es.udc.acarballal.elmas.web.util.UserSession;

public class Logout {
	
	@Inject
	private Cookies cookies;

	@SuppressWarnings("unused")
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@ApplicationState
	private PageSession nextPage;

	Object onActivate() {
		
		userSession = null;
		nextPage = null;
		CookiesManager.removeCookies(cookies);
		return Index.class;
		
	}
}
