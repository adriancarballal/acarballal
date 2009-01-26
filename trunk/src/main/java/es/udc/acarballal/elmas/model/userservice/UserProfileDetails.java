package es.udc.acarballal.elmas.model.userservice;

public class UserProfileDetails {

	private String email;
	private String firstName;
	private String lastName;

	public UserProfileDetails(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	@Override
    public boolean equals(Object obj) {

        if ((obj==null) || !(obj instanceof UserProfileDetails)) {
            return false;
        }

        UserProfileDetails theOther = (UserProfileDetails) obj;

        return firstName.equals(theOther.firstName) && 
        	lastName.equals(theOther.lastName) &&
        	email==theOther.email;
    }
	
	public String getEmail() {
		return email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
    public String getLastName() {
		return lastName;
	}
	
}
