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
import es.udc.acarballal.elmas.web.util.PageSession;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

	@Inject
	private Cookies cookies;

	@Component
	private Form loginForm;

	@Property
	private String loginName;

	private LoginResult loginResult = null;

	@Inject
	private Messages messages;

	@Property
	private String password;

	@Property
	private boolean rememberMyPassword;

	@Inject
	private UserService userService;

	@SuppressWarnings("unused")
	@ApplicationState
	private UserSession userSession;

	@ApplicationState
	private PageSession nextPage;
	
	@SuppressWarnings("unused")
	@Property
	private boolean nextPageExists;

	Object onSuccess() {
		
		userSession.setUserProfileId(loginResult.getUserProfileId());
		userSession.setFirstName(loginResult.getFirstName());
		userSession.setPrivileges(loginResult.getPrivileges());

		if (rememberMyPassword) {
			CookiesManager.leaveCookies(cookies, loginName, loginResult
					.getEncryptedPassword());
		}
		
		if(nextPageExists){
			Class page = nextPage.getPage();
			nextPage = null;
			return page;
		}
		return Index.class;

	}

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

}
