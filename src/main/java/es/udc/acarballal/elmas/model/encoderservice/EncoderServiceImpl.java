package es.udc.acarballal.elmas.model.encoderservice;

import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.model.encoderservice.util.EncodeProcess;
import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile;
import es.udc.acarballal.elmas.model.userprofile.UserProfileDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Transactional
public class EncoderServiceImpl implements EncoderService{
	
	private static final Long AdminMessage = new Long(1);

	private VideoService videoService;
	private UserService userService;
	private UserProfileDao userProfileDao;
	
	
	public void setVideoService(VideoService videoService){
		this.videoService = videoService;
	}
	
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	public void setUserProfileDao(UserProfileDao userProfileDao) {
		this.userProfileDao = userProfileDao;
	}
	
	public void encodeVideo(String fileAbsolutePath, Long userProfileId,
			String title, String comment) throws InstanceNotFoundException, IncorrectPasswordException,
			InsufficientPrivilegesException {
		
		UserProfile userProfile;
		
		userProfile = userProfileDao.find(userProfileId);
		if(userProfile.getPrivileges() == Privileges_TYPES.NONE ||
				userProfile.getPrivileges() == Privileges_TYPES.VOTER){
			throw new InsufficientPrivilegesException(userProfile.getFirstName());
		}
		
		EncodeProcess hilo = new EncodeProcess(fileAbsolutePath, userProfileId,
				title, comment, videoService, this);
		hilo.start();		
	}	
	
	public void sendErrorMessage(Long to, String message)
			throws InstanceNotFoundException {
		userService.sendMessage(AdminMessage, to, message);
	}

	public void sendConfirmationMessage(Long to, String message) 
			throws InstanceNotFoundException {
		userService.sendMessage(AdminMessage, to, message);
	}
}
