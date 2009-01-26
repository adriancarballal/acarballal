package es.udc.acarballal.elmas.web.pages.user;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.userservice.UserProfileDetails;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;

@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Register {

	@Property
	private String email;

	@Property
	private String firstName;

	@Property
	private String lastName;

	@Property
	private String loginName;

	@Component(id = "loginName")
	private TextField loginNameField;

	@Inject
	private Messages messages;

	@Property
	private String password;

	@Component(id = "password")
	private PasswordField passwordField;

	@Component
	private Form registrationForm;

	@Property
	private String retypePassword;

	private Long userProfileId;

	@Inject
	private UserService userService;
	
	@SuppressWarnings("unused")
	@ApplicationState
	private UserSession userSession;

	Object onSuccess() {
		
		userSession.setUserProfileId(userProfileId);
		userSession.setFirstName(firstName);
		return Index.class;
		
	}

	void onValidateForm() {

		if (!registrationForm.isValid()) {
			return;
		}

		if (!password.equals(retypePassword)) {
			registrationForm.recordError(passwordField, messages
					.get("error-passwordsDontMatch"));
		} else {

			try {
				userProfileId = userService.registerUser(loginName, password,
						new UserProfileDetails(firstName, lastName, email));
			} catch (DuplicateInstanceException e) {
				registrationForm.recordError(loginNameField, messages
						.get("error-loginNameAlreadyExists"));
			}

		}

	}

}
