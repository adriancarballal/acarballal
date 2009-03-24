package es.udc.acarballal.elmas.model.userservice;

import java.util.Calendar;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.message.Message;
import es.udc.acarballal.elmas.model.message.MessageDao;
import es.udc.acarballal.elmas.model.usercomment.UserComment;
import es.udc.acarballal.elmas.model.usercomment.UserCommentDao;
import es.udc.acarballal.elmas.model.usercommentcomplaint.UserCommentComplaint;
import es.udc.acarballal.elmas.model.usercommentcomplaint.UserCommentComplaintDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile;
import es.udc.acarballal.elmas.model.userprofile.UserProfileDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Transactional
public class UserServiceImpl implements UserService {

	private UserCommentComplaintDao userCommentComplaintDao;
	private UserCommentDao userCommentDao;
	private UserProfileDao userProfileDao;
	private MessageDao messageDao;

	public void changePassword(Long userProfileId, String oldClearPassword,
			String newClearPassword) throws IncorrectPasswordException,
			InstanceNotFoundException {

		UserProfile userProfile;
		userProfile = userProfileDao.find(userProfileId);

		String storedPassword = userProfile.getEncryptedPassword();

		if (!PasswordEncrypter.isClearPasswordCorrect(oldClearPassword,
				storedPassword)) {
			throw new IncorrectPasswordException(userProfile.getLoginName());
		}

		userProfile.setEncryptedPassword(PasswordEncrypter
				.crypt(newClearPassword));
		userProfileDao.update(userProfile);
	}
	
	public LoginResult changePrivileges(Long userProfileId, 
			Privileges_TYPES privileges) throws InstanceNotFoundException,
			InsufficientPrivilegesException {
		
		UserProfile userProfile;
		userProfile = userProfileDao.find(userProfileId);
		if(privileges == Privileges_TYPES.ADMIN){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}

		userProfile.setPrivileges(privileges);
		userProfileDao.update(userProfile);
		return new LoginResult(userProfile.getUserProfileId(), userProfile
				.getFirstName(), userProfile.getEncryptedPassword(), 
				userProfile.getPrivileges());
	}
	
	public Long commentUser(Long commentatorId, Long commentedId, 
			String comment,	Calendar date) throws InstanceNotFoundException, 
			InsufficientPrivilegesException, InvalidOperationException{
		
		UserProfile commentatorUser = userProfileDao.find(commentatorId);
		UserProfile commentedUser = userProfileDao.find(commentedId);
		if(commentatorUser.getPrivileges()==Privileges_TYPES.NONE){
			throw new InsufficientPrivilegesException(commentatorUser.getLoginName());
		}
		if(commentatorUser.getUserProfileId() == commentedUser.getUserProfileId()){
			throw new InvalidOperationException("Cannot comment yourself " + 
					commentatorUser.getLoginName());
		}
		UserComment userComment = new UserComment(commentatorUser, commentedUser, 
				comment, date);
		userCommentDao.create(userComment);
		return userComment.getCommentId();
	}

	public Long complaintUserComment(Long userCommentId, Long userProfileId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException{
		
		UserProfile userProfile = userProfileDao.find(userProfileId);
		UserComment comment = userCommentDao.find(userCommentId);
		if(userProfile.getPrivileges()==Privileges_TYPES.NONE){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		UserCommentComplaint complaint = 
			new UserCommentComplaint(comment, userProfile, Calendar.getInstance());
		userCommentComplaintDao.create(complaint);
		return complaint.getComplaintId();
	}
	
	@Transactional(readOnly = true)
	public boolean isUserCommentComplaintedBy(Long userId, Long userCommentId){
		return userCommentComplaintDao.hasComplaint(userId, userCommentId);
	}

	public void deleteUserComment(Long commentId, Long userProfileId)
			throws InstanceNotFoundException, InsufficientPrivilegesException{
		
		UserComment comment = userCommentDao.find(commentId);
		UserProfile userProfile = userProfileDao.find(userProfileId);
		if(!comment.getCommentator().getUserProfileId().equals((userProfileId))
				&& userProfile.getPrivileges()!=Privileges_TYPES.ADMIN){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		userCommentDao.remove(commentId);
	}

	@Transactional(readOnly = true)
	public UserCommentBlock findUserCommentsByCommentator(Long userProfileId,
			int startIndex, int count){
		
		List<UserComment> comments = 
			userCommentDao.findCommentByCommentator(userProfileId, startIndex, count+1);
		
		boolean existMoreUserComments = comments.size() == (count + 1);
		
		if(existMoreUserComments){
			comments.remove(comments.size() - 1);
		}
		
		return new UserCommentBlock(comments, existMoreUserComments);
	}
	
	@Transactional(readOnly = true)
	public UserCommentBlock findUserCommentsByCommented(Long userProfileId,
			int startIndex, int count){

		List<UserComment> comments = 
			userCommentDao.findCommentByCommented(userProfileId, startIndex, count+1);
		
		boolean existMoreUserComments = comments.size() == (count + 1);
		
		if(existMoreUserComments){
			comments.remove(comments.size() - 1);
		}
		
		return new UserCommentBlock(comments, existMoreUserComments);
	}
	
	@Transactional(readOnly = true)
	public UserProfileDetails findUserProfileDetails(Long userProfileId)
			throws InstanceNotFoundException {
		UserProfile userProfile;

		userProfile = userProfileDao.find(userProfileId);

		return new UserProfileDetails(userProfile.getFirstName(), userProfile
				.getLastName(), userProfile.getEmail());
	}
	
	@Transactional(readOnly = true)
	public LoginResult login(String loginName, String password,
			boolean passwordIsEncrypted) throws InstanceNotFoundException,
			IncorrectPasswordException {

		UserProfile userProfile = userProfileDao.findByLoginName(loginName);
		String storedPassword = userProfile.getEncryptedPassword();

		if (passwordIsEncrypted) {
			if (!password.equals(storedPassword)) {
				throw new IncorrectPasswordException(loginName);
			}
		} else {
			if (!PasswordEncrypter.isClearPasswordCorrect(password,
					storedPassword)) {
				throw new IncorrectPasswordException(loginName);
			}
		}
		return new LoginResult(userProfile.getUserProfileId(), userProfile
				.getFirstName(), storedPassword, userProfile.getPrivileges());

	}
	
	public Long registerUser(String loginName, String clearPassword,
			UserProfileDetails userProfileDetails)
			throws DuplicateInstanceException {

		try {
			userProfileDao.findByLoginName(loginName);
			throw new DuplicateInstanceException(loginName, UserProfile.class
					.getName());
		} catch (InstanceNotFoundException e) {
			String encryptedPassword = PasswordEncrypter.crypt(clearPassword);

			UserProfile userProfile = new UserProfile(loginName,
					encryptedPassword, userProfileDetails.getFirstName(),
					userProfileDetails.getLastName(), userProfileDetails
							.getEmail());

			userProfileDao.create(userProfile);
			return userProfile.getUserProfileId();
		}

	}
	
	public void setUserCommentComplaintDao(
			UserCommentComplaintDao userCommentComplaintDao){
		this.userCommentComplaintDao = userCommentComplaintDao;
	}

	public void setUserCommentDao(UserCommentDao userCommentDao) {
		this.userCommentDao = userCommentDao;
	}
	
	public void setMessageDao(MessageDao messageDao){
		this.messageDao = messageDao;
	}
	
	public void setUserProfileDao(UserProfileDao userProfileDao) {
		this.userProfileDao = userProfileDao;
	}
	
	public void updateUserProfileDetails(Long userProfileId,
			UserProfileDetails userProfileDetails)
			throws InstanceNotFoundException {

		UserProfile userProfile = userProfileDao.find(userProfileId);
		userProfile.setFirstName(userProfileDetails.getFirstName());
		userProfile.setLastName(userProfileDetails.getLastName());
		userProfile.setEmail(userProfileDetails.getEmail());

		userProfileDao.update(userProfile);
	}

	public void removeMessage(Long messageId, Long userProfileId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException {
		
		UserProfile userProfile = userProfileDao.find(userProfileId);
		Message message = messageDao.find(messageId);
		if(!message.getReceiver().equals(userProfile)){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		messageDao.remove(messageId);
	}

	public Long sendMessage(Long from, Long to, String text) 
			throws InstanceNotFoundException {
		
		UserProfile sender = userProfileDao.find(from);
		UserProfile receiver = userProfileDao.find(to);
		
		Message message = new Message(sender, receiver, text);
		messageDao.create(message);
		
		return message.getId();
	}
	
	@Transactional(readOnly = true)
	public int getInBoxTotal(Long userProfileId){
		return messageDao.inBoxTotal(userProfileId);
	}
	
	@Transactional(readOnly = true)
	public MessageBlock findUserInBox(Long userProfileId, int startIndex, int count){
		
		List<Message> messages = 
			messageDao.getInBox(userProfileId, startIndex, count+1);
		boolean existMoreMessages = messages.size() == (count + 1);
		if(existMoreMessages) messages.remove(messages.size() - 1);
		return new MessageBlock(messages, existMoreMessages);
	}
	
}
