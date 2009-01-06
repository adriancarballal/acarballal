package es.udc.acarballal.elmas.web.services;

import java.io.IOException;

import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.LoginResult;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.util.CookiesManager;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class SessionDispatcher implements Dispatcher {

	private ApplicationStateManager applicationStateManager;
	private Cookies cookies;
	private UserService userService;

	public SessionDispatcher(ApplicationStateManager applicationStateManager,
			Cookies cookies, UserService userService) {
		this.applicationStateManager = applicationStateManager;
		this.cookies = cookies;
		this.userService = userService;
	}

	public boolean dispatch(Request request, Response response)
			throws IOException {
		
		if (!applicationStateManager.exists(UserSession.class)) {
			
			String loginName = CookiesManager.getLoginName(cookies);
			if (loginName == null) {
				return false;
			}

			String encryptedPassword = CookiesManager
					.getEncryptedPassword(cookies);
			if (encryptedPassword == null) {
				return false;
			}
			
			Privileges_TYPES privileges = CookiesManager.getPrivileges(cookies);

			try {

				LoginResult loginResult = userService.login(loginName,
						encryptedPassword, true);
				UserSession userSession = new UserSession();
				userSession.setUserProfileId(loginResult.getUserProfileId());
				userSession.setFirstName(loginResult.getFirstName());
				userSession.setPrivileges(loginResult.getPrivileges());
				applicationStateManager.set(UserSession.class, userSession);

			} catch (InstanceNotFoundException e) {
				CookiesManager.removeCookies(cookies);
			} catch (IncorrectPasswordException e) {
				CookiesManager.removeCookies(cookies);
			}

		}

		return false;

	}

}
