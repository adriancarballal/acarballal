package es.udc.acarballal.elmas.model.userservice;

import java.util.List;

import es.udc.acarballal.elmas.model.userprofile.UserProfile;

public class UserProfileBlock {

	private boolean existMoreUsers;
    private List<UserProfile> users;
    
    public UserProfileBlock(List<UserProfile> users, boolean existMoreUsers){
    	this.users = users;
    	this.existMoreUsers = existMoreUsers;
    }

	public boolean getExistMoreUsers() {
		return existMoreUsers;
	}

	public List<UserProfile> getUsers() {
		return users;
	}
    
    
    
    
}
