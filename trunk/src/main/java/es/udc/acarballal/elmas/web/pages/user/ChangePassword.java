package es.udc.acarballal.elmas.web.pages.user;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.CookiesManager;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ChangePassword {

	@Component
	private Form changePasswordForm;

	@Inject
	private Cookies cookies;

	@Inject
	private Messages messages;

	@Property
	private String newPassword;

	@Property
	private String oldPassword;

	@Property
	private String retypeNewPassword;

	@Inject
	private UserService userService;

	@ApplicationState
	private UserSession userSession;

	Object onSuccess() {

		CookiesManager.removeCookies(cookies);
		return Index.class;

	}

	void onValidateForm() throws InstanceNotFoundException {

		if (!changePasswordForm.isValid()) {
			return;
		}

		if (!newPassword.equals(retypeNewPassword)) {
			changePasswordForm
					.recordError(messages.get("error-passwordsDontMatch"));
		} else {

			try {
				userService.changePassword(userSession.getUserProfileId(),
						oldPassword, newPassword);
			} catch (IncorrectPasswordException e) {
				changePasswordForm.recordError(messages
						.get("error-invalidPassword"));
			}

		}

	}

}
