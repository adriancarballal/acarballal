package es.udc.acarballal.elmas.web.pages.user;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.UserProfileDetails;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.pages.Index;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
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
	
	@Property
	private boolean participate;

	@ApplicationState
	private UserSession userSession;

	@Inject
	private UserService userService;

	void onPrepareForRender() throws InstanceNotFoundException {

		UserProfileDetails userProfile;
		
		userProfile = userService.findUserProfileDetails(userSession
				.getUserProfileId());
		firstName = userProfile.getFirstName();
		lastName = userProfile.getLastName();
		email = userProfile.getEmail();
		if(userSession.getPrivileges()==Privileges_TYPES.ADMIN ||
					userSession.getPrivileges()==Privileges_TYPES.COMPETITOR)
			participate = true;
		else participate = false;
		
	}

	Object onSuccess() {

		Privileges_TYPES privileges;
		if(participate) privileges=Privileges_TYPES.COMPETITOR;
		else privileges=Privileges_TYPES.VOTER;
		
		try {
			userService.updateUserProfileDetails(
				userSession.getUserProfileId(), new UserProfileDetails(
						firstName, lastName, email));
			userSession.setFirstName(firstName);
			userService.changePrivileges(userSession.getUserProfileId(), privileges);
			userSession.setPrivileges(privileges);
			
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		}		
		return Index.class;

	}

}