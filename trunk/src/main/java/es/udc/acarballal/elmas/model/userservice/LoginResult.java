package es.udc.acarballal.elmas.model.userservice;

import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;

public class LoginResult {

	private Long userProfileId;
	private String firstName;
	private String encryptedPassword;
	private Privileges_TYPES privileges;

	public LoginResult(Long userProfileId, String firstName,
			String encryptedPassword, Privileges_TYPES privileges) {
		this.userProfileId = userProfileId;
		this.firstName = firstName;
		this.encryptedPassword = encryptedPassword;
		this.privileges = privileges;
	}
	
	public Long getUserProfileId() {
		return userProfileId;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public Privileges_TYPES getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Privileges_TYPES privileges) {
		this.privileges = privileges;
	}
	
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof LoginResult)) {
			return false;
		}

		LoginResult theOther = (LoginResult) obj;

		return userProfileId.equals(theOther.userProfileId)
				&& (encryptedPassword != null)
				&& encryptedPassword.equals(theOther.encryptedPassword)
				&& (firstName != null) && firstName.equals(theOther.firstName)
				&& (privileges != null) && privileges.equals(theOther.privileges);
	}
	
	public String toString(){
		return userProfileId + " " + encryptedPassword + " " + firstName 
			+ " P:" + privileges;
	}

}
