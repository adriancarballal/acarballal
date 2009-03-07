package es.udc.acarballal.elmas.model.adminservice;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaint;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface AdminService {
	
	public void deleteUserProfile(Long deleteUserId, Long userProfileId)
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public void deleteUserCommentComplaint(Long id, Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public void deleteVideoCommentComplaint(Long id, Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public void deleteVideoComplaints(Long id, Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public VideoComplaint findFirstVideoComplaints();
	
	public UserCommentComplaintBlock findUserCommentComplaints(int startIndex, int count);
	
	public VideoCommentComplaintBlock findVideoCommentComplaints(int startIndex, int count);
	
	public int getNumberOfUserCommentComplaints(Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public int getNumberOfVideoCommentComplaints(Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public int getNumberOfVideoComplaints(Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;

}
