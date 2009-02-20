package es.udc.acarballal.elmas.web.pages.mobile.user;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.acarballal.elmas.web.pages.mobile.Index;
import es.udc.acarballal.elmas.web.util.CookiesManager;
import es.udc.acarballal.elmas.web.util.UserSession;

public class Logout {
	
	@Inject
	private Cookies cookies;

	@SuppressWarnings("unused")
	@ApplicationState
	private UserSession userSession;

	Object onActivate() {
		
		userSession = null;
		CookiesManager.removeCookies(cookies);
		return Index.class;
		
	}

}
