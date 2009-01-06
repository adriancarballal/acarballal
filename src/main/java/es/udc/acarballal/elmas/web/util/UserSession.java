package es.udc.acarballal.elmas.web.util;

import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;


public class UserSession {

	private Long userProfileId;
	private String firstName;
	private Privileges_TYPES privileges;
	
	public Long getUserProfileId() {
		return userProfileId;
	}
	
	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Privileges_TYPES getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Privileges_TYPES privileges) {
		this.privileges = privileges;
	}

}
