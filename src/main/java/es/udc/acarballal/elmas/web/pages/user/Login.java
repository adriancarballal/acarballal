package es.udc.acarballal.elmas.web.pages.user;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.userservice.LoginResult;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.CookiesManager;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

	@Property
	private String loginName;

	@Property
	private String password;

	@Property
	private boolean rememberMyPassword;

	@SuppressWarnings("unused")
	@ApplicationState
	private UserSession userSession;

	@Inject
	private Cookies cookies;

	@Component
	private Form loginForm;

	@Inject
	private Messages messages;

	@Inject
	private UserService userService;

	private LoginResult loginResult = null;


	void onValidateForm() {

		if (!loginForm.isValid()) {
			return;
		}

		try {
			loginResult = userService.login(loginName, password, false);
		} catch (InstanceNotFoundException e) {
			loginForm.recordError(messages.get("error-authenticationFailed"));
		} catch (IncorrectPasswordException e) {
			loginForm.recordError(messages.get("error-authenticationFailed"));
		}

	}

	Object onSuccess() {

		userSession.setUserProfileId(loginResult.getUserProfileId());
		userSession.setFirstName(loginResult.getFirstName());
		userSession.setPrivileges(loginResult.getPrivileges());

		if (rememberMyPassword) {
			CookiesManager.leaveCookies(cookies, loginName, loginResult
					.getEncryptedPassword(), loginResult.getPrivileges().toString());
		}
		return Index.class;

	}

}
