package es.udc.acarballal.elmas.web.util;

import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;


public class UserSession {

	private String firstName;
	private Privileges_TYPES privileges;
	private Long userProfileId;
	
	public String getFirstName() {
		return firstName;
	}
	
	public Privileges_TYPES getPrivileges() {
		return privileges;
	}
	
	public Long getUserProfileId() {
		return userProfileId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setPrivileges(Privileges_TYPES privileges) {
		this.privileges = privileges;
	}
	
	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}

}
