package es.udc.acarballal.elmas.model.adminservice;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.model.adminservice.util.EncodeProcess;
import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.message.Message;
import es.udc.acarballal.elmas.model.message.MessageDao;
import es.udc.acarballal.elmas.model.usercommentcomplaint.UserCommentComplaint;
import es.udc.acarballal.elmas.model.usercommentcomplaint.UserCommentComplaintDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile;
import es.udc.acarballal.elmas.model.userprofile.UserProfileDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.videocommentcomplaint.VideoCommentComplaint;
import es.udc.acarballal.elmas.model.videocommentcomplaint.VideoCommentComplaintDao;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaint;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaintDao;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Transactional
public class AdminServiceImpl implements AdminService{
	
	private static final Long AdminProfileId = new Long(1); 
	
	private UserCommentComplaintDao userCommentComplaintDao;
	private UserProfileDao userProfileDao;
	private VideoCommentComplaintDao videoCommentComplaintDao;
	private VideoComplaintDao videoComplaintDao;
	private MessageDao messageDao;
	
	public void encodeVideo(String fileAbsolutePath, Long userProfileId,
			String title, String comment, VideoService videoService) 
			throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException {
		
		UserProfile userProfile;
		
		userProfile = userProfileDao.find(userProfileId);
		if(userProfile.getPrivileges() == Privileges_TYPES.NONE ||
				userProfile.getPrivileges() == Privileges_TYPES.VOTER){
			throw new InsufficientPrivilegesException(userProfile.getFirstName());
		}
		
		EncodeProcess hilo = new EncodeProcess(fileAbsolutePath, userProfileId,
				title, comment, this, videoService);
		hilo.start();		
	}
	
	public void deleteUserProfile(Long deleteUserId, Long userProfileId)
		throws InsufficientPrivilegesException, InstanceNotFoundException{
		
		UserProfile userProfile;
		UserProfile deleteProfile;
		try {
			userProfile = userProfileDao.find(userProfileId); 
			deleteProfile = userProfileDao.find(deleteUserId);
			if (userProfile.getPrivileges() == Privileges_TYPES.ADMIN &&
					deleteProfile.getPrivileges()!= Privileges_TYPES.ADMIN){
				userProfileDao.remove(deleteUserId);
			}
			else{
				throw new InsufficientPrivilegesException(userProfile.getLoginName());
			}
		} catch (InstanceNotFoundException e) {
			throw e;
		}
		
	}
	
	public void deleteUserCommentComplaint(Long id, Long userProfileId) 
			throws InsufficientPrivilegesException, InstanceNotFoundException{
		
		UserProfile userProfile;
		try {
			userProfile = userProfileDao.find(userProfileId); 
			if (userProfile.getPrivileges() == Privileges_TYPES.ADMIN){
				userCommentComplaintDao.remove(id);
			}
			else{
				throw new InsufficientPrivilegesException(userProfile.getLoginName());
			}
		} catch (InstanceNotFoundException e) {
			throw e;
		}
	}
	
	public void deleteVideoCommentComplaint(Long id, Long userProfileId) 
			throws InsufficientPrivilegesException, InstanceNotFoundException{

		UserProfile userProfile;
		try {
			userProfile = userProfileDao.find(userProfileId); 
			if (userProfile.getPrivileges() == Privileges_TYPES.ADMIN){
				videoCommentComplaintDao.remove(id);
			}
			else{
				throw new InsufficientPrivilegesException(userProfile.getLoginName());
			}
		} catch (InstanceNotFoundException e) {
			throw e;
		}
	}

	public void deleteVideoComplaint(Long id, Long userProfileId) 
			throws InsufficientPrivilegesException, InstanceNotFoundException{
		
		UserProfile userProfile;
		try {
			userProfile = userProfileDao.find(userProfileId); 
			if (userProfile.getPrivileges() == Privileges_TYPES.ADMIN){
				videoComplaintDao.remove(id);
			}
			else{
				throw new InsufficientPrivilegesException(userProfile.getLoginName());
			}
		} catch (InstanceNotFoundException e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = true)
	public VideoComplaint findFirstVideoComplaints(Long userProfileId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException{
		
		UserProfile userProfile;
		List<VideoComplaint> complaints = null;
		try {
			userProfile = userProfileDao.find(userProfileId); 
			if (userProfile.getPrivileges() == Privileges_TYPES.ADMIN){
				int startIndex = 0;
				int count = 1;
				complaints =
					videoComplaintDao.findVideoComplaints(startIndex, count);
			} 
			else{
				throw new InsufficientPrivilegesException(userProfile.getLoginName());
			}
		}catch (InstanceNotFoundException e) {	throw e;	}
		if(complaints.isEmpty()) return null;
		return complaints.get(0);
	}

	public UserCommentComplaintBlock findUserCommentComplaints(
			Long userProfileId, int startIndex, int count) 
			throws InsufficientPrivilegesException, InstanceNotFoundException{
		
		UserProfile userProfile;
		try {
			userProfile = userProfileDao.find(userProfileId); 
			if (userProfile.getPrivileges() != Privileges_TYPES.ADMIN){
				throw new InsufficientPrivilegesException(userProfile.getLoginName());
			}
		}catch (InstanceNotFoundException e) {	throw e;	}
		List<UserCommentComplaint> comments = 
			userCommentComplaintDao.findUserCommentComplaints(startIndex, count+1);

		boolean existMoreUserCommentsComplaints = comments.size() == (count + 1);

		if (existMoreUserCommentsComplaints) {
			comments.remove(comments.size() - 1);
		}
		
		return new UserCommentComplaintBlock(comments, existMoreUserCommentsComplaints);
	}
	
	public VideoCommentComplaintBlock findVideoCommentComplaints(
			Long userProfileId, int startIndex, int count) 
			throws InsufficientPrivilegesException, InstanceNotFoundException{
		
		UserProfile userProfile;
		try {
			userProfile = userProfileDao.find(userProfileId); 
			if (userProfile.getPrivileges() != Privileges_TYPES.ADMIN){
				throw new InsufficientPrivilegesException(userProfile.getLoginName());
			}
		}catch (InstanceNotFoundException e) {	throw e;	}
		
		List<VideoCommentComplaint> comments = 
			videoCommentComplaintDao.findVideoCommentComplaints(startIndex, count+1);

		boolean existMoreVideoCommentsComplaints = comments.size() == (count + 1);

		if (existMoreVideoCommentsComplaints) {
			comments.remove(comments.size() - 1);
		}
		
		return new VideoCommentComplaintBlock(comments, existMoreVideoCommentsComplaints);
	}
	
	public int getNumberOfUserCommentComplaints(Long userProfileId) 
			throws InsufficientPrivilegesException, InstanceNotFoundException{

		UserProfile userProfile;
		try {
			userProfile = userProfileDao.find(userProfileId); 
			if (userProfile.getPrivileges() == Privileges_TYPES.ADMIN){
				return userCommentComplaintDao.countUserCommentComplaints();
			}
			else{
				throw new InsufficientPrivilegesException(userProfile.getLoginName());
			}
		} catch (InstanceNotFoundException e) {
			throw e;
		}
	}
	
	public int getNumberOfVideoCommentComplaints(Long userProfileId) 
			throws InsufficientPrivilegesException, InstanceNotFoundException{

		UserProfile userProfile;
		try {
			userProfile = userProfileDao.find(userProfileId); 
			if (userProfile.getPrivileges() == Privileges_TYPES.ADMIN){
				return videoCommentComplaintDao.countVideoCommentComplaints();
			}
			else{
				throw new InsufficientPrivilegesException(userProfile.getLoginName());
			}
		} catch (InstanceNotFoundException e) {
			throw e;
		}
	}
	
	public int getNumberOfVideoComplaints(Long userProfileId) 
			throws InsufficientPrivilegesException, InstanceNotFoundException{
		
		UserProfile userProfile;
		try {
			userProfile = userProfileDao.find(userProfileId); 
			if (userProfile.getPrivileges() == Privileges_TYPES.ADMIN){
				return videoComplaintDao.countVideoComplaints();
			}
			else{
				throw new InsufficientPrivilegesException(userProfile.getLoginName());
			}
		} catch (InstanceNotFoundException e) {
			throw e;
		}
	}
	
	public void sendErrorMessage(Long to, String message)
			throws InstanceNotFoundException {
		
		UserProfile sender = userProfileDao.find(AdminProfileId);
		UserProfile receiver = userProfileDao.find(to);
	
		Message m = new Message(sender, receiver, message);
		messageDao.create(m);
}

	public void sendConfirmationMessage(Long to, String message) 
			throws InstanceNotFoundException {
	
		UserProfile sender = userProfileDao.find(AdminProfileId);
		UserProfile receiver = userProfileDao.find(to);
	
		Message m = new Message(sender, receiver, message);
		messageDao.create(m);
	}

	
	public void setUserCommentComplaintDao(
			UserCommentComplaintDao userCommentComplaintDao){
		this.userCommentComplaintDao = userCommentComplaintDao;
	}

	public void setUserProfileDao(UserProfileDao userProfileDao) {
		this.userProfileDao = userProfileDao;
	}
	
	public void setVideoCommentComplaintDao(
			VideoCommentComplaintDao videoCommentComplaintDao){
		this.videoCommentComplaintDao = videoCommentComplaintDao;
	}
	
	public void setVideoComplaintDao(VideoComplaintDao videoComplaintDao) {
		this.videoComplaintDao = videoComplaintDao;
	}
	
	public void setMessageDao(MessageDao messageDao){
		this.messageDao = messageDao;
	}
}
