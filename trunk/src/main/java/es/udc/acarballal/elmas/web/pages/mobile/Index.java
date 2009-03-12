package es.udc.acarballal.elmas.web.pages.mobile;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.LoginResult;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class Index {

	@Component
	private Form loginForm;

	@Property
	private String loginName;

	private LoginResult loginResult = null;

	@Inject
	private Messages messages;

	@Property
	private String password;

	@Inject
	private UserService userService;

	@SuppressWarnings("unused")
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;


	Object onSuccess() {

		userSession.setUserProfileId(loginResult.getUserProfileId());
		userSession.setFirstName(loginResult.getFirstName());
		userSession.setPrivileges(loginResult.getPrivileges());

		return Index.class;

	}

	public boolean isParticipant(){
		return userSessionExists && (
				userSession.getPrivileges()==Privileges_TYPES.COMPETITOR ||
				userSession.getPrivileges()==Privileges_TYPES.ADMIN);
	}
	void onValidateForm() {

		if (!loginForm.isValid()) return;
		try {
			loginResult = userService.login(loginName, password, false);
		} catch (InstanceNotFoundException e) {
			loginForm.recordError(messages.get("error-authenticationFailed"));
		} catch (IncorrectPasswordException e) {
			loginForm.recordError(messages.get("error-authenticationFailed"));
		}

	}

}
