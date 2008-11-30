package es.udc.acarballal.elmas.model.videoservice;

import java.util.Calendar;

import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile;
import es.udc.acarballal.elmas.model.userprofile.UserProfileDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.video.VideoDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class VideoServiceImpl implements VideoService{

	private VideoDao videoDao;
	private UserProfileDao userProfileDao;

	public void setVideoDao(VideoDao videoDao) {
		this.videoDao = videoDao;
	}
	
	public void setUserProfileDao(UserProfileDao userProfileDao){
		this.userProfileDao = userProfileDao;
	}
	
	public Long addVideo(long userId, String title, String comment, 
			String snapshot, Calendar date)
			throws InstanceNotFoundException, InsufficientPrivilegesException{
		
		UserProfile userProfile = userProfileDao.find(userId);
		
		if(userProfile.getPrivileges()!=Privileges_TYPES.COMPETITOR){
			throw new InsufficientPrivilegesException(userProfile.getLoginName());
		}
		Video video = new Video(userProfile, title, comment, snapshot, date);
		videoDao.create(video);
		return video.getVideoId();
	}
	
	@Transactional(readOnly = true)
	public Video findVideoById(long videoId) throws InstanceNotFoundException{
		return videoDao.find(videoId);
	}
	
	public void deleteVideo(long videoId, long userId) 
			throws InstanceNotFoundException, InsufficientPrivilegesException{

		UserProfile userProfile = userProfileDao.find(userId);
		if(userProfile.getPrivileges()!=Privileges_TYPES.ADMIN){
			throw new InsufficientPrivilegesException(userProfile.getLoginName()); 
		}
		videoDao.remove(videoId);
	}
	
	
}
