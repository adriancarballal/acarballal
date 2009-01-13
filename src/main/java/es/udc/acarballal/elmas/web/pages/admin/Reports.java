package es.udc.acarballal.elmas.web.pages.admin;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class Reports {
	
	private int totalVideoComplaints;
	private int totalVideoCommentComplaints;
	private int totalUserCommentComplaints;

	@ApplicationState
	private UserSession userSession;
	
	@Inject
	private AdminService adminService;
	
	void onActivate() {
		try {
			this.totalVideoComplaints = 
				adminService.getNumberOfVideoComplaints(
						userSession.getUserProfileId());
			this.totalVideoCommentComplaints = 
				adminService.getNumberOfVideoCommentComplaints(
						userSession.getUserProfileId());
			this.totalUserCommentComplaints =
				adminService.getNumberOfUserCommentComplaints(
						userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InsufficientPrivilegesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getTotalVideoComplaints(){
		return totalVideoComplaints;
	}
	
	public boolean getExistVideoComplaints(){
		return totalVideoComplaints>0;
	}
	public int getTotalVideoCommentComplaints(){
		return totalVideoCommentComplaints;
	}
	
	public boolean getExistVideoCommentComplaints(){
		return totalVideoCommentComplaints>0;
	}
	public int getTotalUserCommentComplaints(){
		return totalUserCommentComplaints;
	}
	
	public boolean getExistUserCommentComplaints(){
		return totalUserCommentComplaints>0;
	}
}
