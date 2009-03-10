package es.udc.acarballal.elmas.model.userservice;

import java.util.Calendar;

import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface UserService {

	public void changePassword(Long userProfileId, String oldClearPassword,
			String newClearPassword) throws IncorrectPasswordException,
			InstanceNotFoundException;

	public LoginResult changePrivileges(Long userProfileId, Privileges_TYPES privileges) 
			throws InstanceNotFoundException, InsufficientPrivilegesException;

	//A�adir un adminService para este servicio?
	public LoginResult changePrivilegesToAdmin(Long adminId, Long userProfileId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException;

	public Long commentUser(Long commentatorId, Long commentedId, 
			String comment,	Calendar date) throws InstanceNotFoundException, 
			InsufficientPrivilegesException, InvalidOperationException;

	public void complaintUserComment(Long userCommentId, Long userProfileId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	public void deleteUserComment(Long commentId, Long userProfileId)
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	//A�adir un adminService para este servicio? no es necesario!!!
	public UserProfileBlock findAllAdmin(int startIndex, int count);
	
	//A�adir un adminService para este servicio? no es necesario!!!
	public UserProfileBlock findNonAdmin(int startIndex, int count);
	
	public UserCommentBlock findUserCommentsByCommentator(Long userProfileId,
			int startIndex, int count);

	public UserCommentBlock findUserCommentsByCommented(Long userProfileId,
			int startIndex, int count);
	
	public UserProfileDetails findUserProfileDetails(Long userProfileId)
			throws InstanceNotFoundException;
	
	public LoginResult login(String loginName, String password,
			boolean passwordIsEncrypted) throws InstanceNotFoundException,
			IncorrectPasswordException;
	
	public Long registerUser(String loginName, String clearPassword,
			UserProfileDetails userProfileDetails)
			throws DuplicateInstanceException;
	
	public void updateUserProfileDetails(Long userProfileId,
			UserProfileDetails userProfileDetails)
			throws InstanceNotFoundException;
	
	
	/*MESSAGE SERVICE */
	//TODO
	public Long sendMessage(Long from, Long to, String text) 
			throws InstanceNotFoundException;
	
	public void removeMessage(Long messageId, Long userProfileId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	public int getInBoxTotal(Long userProfileId);
	
	public MessageBlock findUserInBox(Long userProfileId, int startIndex, int count);
	
}
