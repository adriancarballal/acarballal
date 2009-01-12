package es.udc.acarballal.elmas.model.adminservice;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaint;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface AdminService {
	
	public int getNumberOfVideoComplaints(Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public int getNumberOfVideoCommentComplaints(Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public int getNumberOfUserCommentComplaints(Long userProfileId) 
	throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public VideoComplaint findFirstVideoComplaints();
	
	public void deleteVideoComplaints(Long id, Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public UserCommentComplaintBlock findUserCommentComplaints(int startIndex, int count);
	
	public void deleteUserCommentComplaint(Long id, Long userProfileId) 
	throws InsufficientPrivilegesException, InstanceNotFoundException;

}
