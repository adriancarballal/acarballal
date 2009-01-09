package es.udc.acarballal.elmas.model.adminservice;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface AdminService {
	
	public int getNumberOfVideoComplaints() 
		throws InsufficientPrivilegesException, InstanceNotFoundException;
	
	public void deleteVideoComplaints(Long id, Long userProfileId) 
		throws InsufficientPrivilegesException, InstanceNotFoundException;

}
