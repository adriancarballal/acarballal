package es.udc.acarballal.elmas.model.userservice;

import java.util.Calendar;

import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface UserService {

	public Long registerUser(String loginName, String clearPassword,
			UserProfileDetails userProfileDetails)
			throws DuplicateInstanceException;

	public LoginResult login(String loginName, String password,
			boolean passwordIsEncrypted) throws InstanceNotFoundException,
			IncorrectPasswordException;

	public UserProfileDetails findUserProfileDetails(Long userProfileId)
			throws InstanceNotFoundException;

	public void updateUserProfileDetails(Long userProfileId,
			UserProfileDetails userProfileDetails)
			throws InstanceNotFoundException;

	public void changePassword(Long userProfileId, String oldClearPassword,
			String newClearPassword) throws IncorrectPasswordException,
			InstanceNotFoundException;
	
	public LoginResult changePrivileges(Long userProfileId, Privileges_TYPES privileges) 
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	//Añadir un adminService para este servicio?
	public LoginResult changePrivilegesToAdmin(Long adminId, Long userProfileId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	public Long commentUser(Long commentatorId, Long commentedId, 
			String comment,	Calendar date) 
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	//Añadir un adminService para este servicio?
	public void deleteUserComment(Long commentId, Long userProfileId)
			throws InstanceNotFoundException, InsufficientPrivilegesException;

}
