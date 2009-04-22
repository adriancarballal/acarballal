package es.udc.acarballal.elmas.model.adminservice;

import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaint;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface AdminService {
	
	public void encodeVideo(String fileAbsolutePath, Long userProfileId,
		String title, String comment, VideoService videoService) 
		throws InstanceNotFoundException, IncorrectPasswordException,	
		InsufficientPrivilegesException;
			
	public void deleteUserProfile(Long deleteUserId, Long userProfileId)
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public void deleteUserCommentComplaint(Long id, Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public void deleteVideoCommentComplaint(Long id, Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public void deleteVideoComplaint(Long id, Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public VideoComplaint findFirstVideoComplaints(Long userProfileId)
		throws InstanceNotFoundException, InsufficientPrivilegesException;
	
	public UserCommentComplaintBlock findUserCommentComplaints(
			Long userProfileId, int startIndex, int count) 
			throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public VideoCommentComplaintBlock findVideoCommentComplaints(
			Long userProfileId, int startIndex, int count) 
			throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public int getNumberOfUserCommentComplaints(Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public int getNumberOfVideoCommentComplaints(Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public int getNumberOfVideoComplaints(Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public void sendConfirmationMessage(Long to, String message) 
		throws InstanceNotFoundException;

	public void sendErrorMessage(Long to, String message) 
		throws InstanceNotFoundException;

}
