package es.udc.acarballal.elmas.web.pages.user;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.userservice.UserProfileDetails;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class UpdateProfile {

	@Property
	private String firstName;

	@Property
	private String lastName;

	@Property
	private String email;

	@ApplicationState
	private UserSession userSession;

	@Inject
	private UserService userService;

	void onPrepareForRender() throws InstanceNotFoundException {

		UserProfileDetails userProfile;
		System.out.println("PRUEBA");
		userProfile = userService.findUserProfileDetails(userSession
				.getUserProfileId());
		firstName = userProfile.getFirstName();
		lastName = userProfile.getLastName();
		email = userProfile.getEmail();

	}

	Object onSuccess() throws InstanceNotFoundException {
		
		userService.updateUserProfileDetails(
				userSession.getUserProfileId(), new UserProfileDetails(
						firstName, lastName, email));
		userSession.setFirstName(firstName);
		return Index.class;

	}

}