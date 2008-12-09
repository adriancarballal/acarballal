package es.udc.acarballal.elmas.model.userservice;

import java.util.List;

import es.udc.acarballal.elmas.model.userprofile.UserProfile;

public class UserProfileBlock {

	private List<UserProfile> users;
    private boolean existMoreUsers;
    
    public UserProfileBlock(List<UserProfile> users, boolean existMoreUsers){
    	this.users = users;
    	this.existMoreUsers = existMoreUsers;
    }

	public List<UserProfile> getUsers() {
		return users;
	}

	public boolean getExistMoreUsers() {
		return existMoreUsers;
	}
    
    
    
    
}
