package es.udc.acarballal.elmas.model.adminservice;

import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.usercommentcomplaint.UserCommentComplaintDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile;
import es.udc.acarballal.elmas.model.userprofile.UserProfileDao;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.videocommentcomplaint.VideoCommentComplaintDao;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaintDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Transactional
public class AdminServiceImpl implements AdminService{

	private UserProfileDao userProfileDao;
	private VideoComplaintDao videoComplaintDao;
	private VideoCommentComplaintDao videoCommentComplaintDao;
	private UserCommentComplaintDao userCommentComplaintDao;
	
	public void setUserProfileDao(UserProfileDao userProfileDao) {
		this.userProfileDao = userProfileDao;
	}
	
	public void setVideoComplaintDao(VideoComplaintDao videoComplaintDao) {
		this.videoComplaintDao = videoComplaintDao;
	}

	public void setVideoCommentComplaintDao(
			VideoCommentComplaintDao videoCommentComplaintDao){
		this.videoCommentComplaintDao = videoCommentComplaintDao;
	}
	
	public void setUserCommentComplaintDao(
			UserCommentComplaintDao userCommentComplaintDao){
		this.userCommentComplaintDao = userCommentComplaintDao;
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
	
	public void deleteVideoComplaints(Long id, Long userProfileId) 
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
}
