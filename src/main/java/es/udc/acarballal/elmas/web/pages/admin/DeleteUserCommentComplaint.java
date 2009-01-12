package es.udc.acarballal.elmas.web.pages.admin;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class DeleteUserCommentComplaint {

	@ApplicationState
	private UserSession userSession;
	
	@Inject
	private AdminService adminService;
	
	Object onActivate(Long userCommentComplaintId){
		try {
			adminService.deleteUserCommentComplaint(
					userCommentComplaintId, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			//System.out.println("--- Error 1 ---");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InsufficientPrivilegesException e) {
			//System.out.println("--- Error 2 ---");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ShowUserCommentComplaint.class;
	}
}
