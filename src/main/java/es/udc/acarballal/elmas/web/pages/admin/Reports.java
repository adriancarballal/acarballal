package es.udc.acarballal.elmas.web.pages.admin;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class Reports {
	
	public enum SHOW_BEST_TYPE {
		ALL, WEEKLY
	}
	
	@Inject
	private AdminService adminService;
	@Property
	private int findNumber;
	private SHOW_BEST_TYPE findType = SHOW_BEST_TYPE.WEEKLY;
	@InjectPage
	private ShowBestVideos showBestVideos;
	
	private int totalUserCommentComplaints;
	
	private int totalVideoCommentComplaints;
	
	private int totalVideoComplaints;

	@ApplicationState
	private UserSession userSession;	
	
	public SHOW_BEST_TYPE getAll(){
		return SHOW_BEST_TYPE.ALL;
	}
	
	public boolean getExistUserCommentComplaints(){
		return totalUserCommentComplaints>0;
	}
	
	public boolean getExistVideoCommentComplaints(){
		return totalVideoCommentComplaints>0;
	}
	
	public boolean getExistVideoComplaints(){
		return totalVideoComplaints>0;
	}
	
	public SHOW_BEST_TYPE getFindType() {
		return findType;
	}
	
	public int getTotalUserCommentComplaints(){
		return totalUserCommentComplaints;
	}
	
	public int getTotalVideoCommentComplaints(){
		return totalVideoCommentComplaints;
	}
	
	public int getTotalVideoComplaints(){
		return totalVideoComplaints;
	}
	public SHOW_BEST_TYPE getWeekly(){
		return SHOW_BEST_TYPE.WEEKLY;
	}
	
	Object onSuccess(){
		showBestVideos.setType(this.findType);
		showBestVideos.setCount(this.findNumber);
		return showBestVideos;

	}
	public void setFindType(SHOW_BEST_TYPE findType) {
		this.findType = findType;
	}
	
	void setupRender() throws InstanceNotFoundException, InsufficientPrivilegesException {
		this.totalVideoComplaints = 
			adminService.getNumberOfVideoComplaints(
					userSession.getUserProfileId());
		this.totalVideoCommentComplaints = 
			adminService.getNumberOfVideoCommentComplaints(
					userSession.getUserProfileId());
		this.totalUserCommentComplaints =
			adminService.getNumberOfUserCommentComplaints(
					userSession.getUserProfileId());
	}
}
