package es.udc.acarballal.elmas.model.userprofile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
public class UserProfile {
	
	public enum Privileges_TYPES {ADMIN, COMPETITOR, NONE, VOTER}

	private String email;
	private String encryptedPassword;
	private String firstName;
	private String lastName;
	private String loginName;
	private Privileges_TYPES privileges;
	private Long userProfileId; 
	private long version;

	public UserProfile() {
	}

	public UserProfile(String loginName, String encryptedPassword,
			String firstName, String lastName, String email) {

		/**
		 * NOTE: "userProfileId" *must* be left as "null" since its value is
		 * automatically generated.
		 */

		this.loginName = loginName;
		this.encryptedPassword = encryptedPassword;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.privileges = Privileges_TYPES.NONE;
	}

	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof UserProfile)) {
			return false;
		}

		UserProfile theOther = (UserProfile) obj;

		return (loginName != null)
				&& userProfileId.equals(theOther.userProfileId)
				&& loginName.equals(theOther.loginName)
				&& (encryptedPassword != null)
				&& encryptedPassword.equals(theOther.encryptedPassword)
				&& (firstName != null) && firstName.equals(theOther.firstName)
				&& (lastName != null) && lastName.equals(theOther.lastName)
				&& (email != null) && email.equals(theOther.email)
				&& (privileges != null) && privileges == theOther.privileges
				&& version == theOther.version;
	}

	public String getEmail() {
		return email;
	}

	@Column(name = "enPassword")
	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLoginName() {
		return loginName;
	}

	public Privileges_TYPES getPrivileges() {
		return privileges;
	}

	@Column(name = "usrId")
	@SequenceGenerator( // It only takes effect for
	name = "UserProfileIdGenerator", // databases providing identifier
	sequenceName = "UserProfileSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "UserProfileIdGenerator")
	public Long getUserProfileId() {
		return userProfileId;
	}

	@Version
	public long getVersion() {
		return version;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void setPrivileges(Privileges_TYPES privileges) {
		this.privileges = privileges;
	}

	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
