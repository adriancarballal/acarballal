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
		WEEKLY, ALL
	}
	
	private int totalVideoComplaints;
	private int totalVideoCommentComplaints;
	private int totalUserCommentComplaints;
	private SHOW_BEST_TYPE findType = SHOW_BEST_TYPE.WEEKLY;
	
	@ApplicationState
	private UserSession userSession;
	
	@Inject
	private AdminService adminService;
	
	public SHOW_BEST_TYPE getFindType() {
		return findType;
	}

	public void setFindType(SHOW_BEST_TYPE findType) {
		this.findType = findType;
	}	
	
	public SHOW_BEST_TYPE getWeekly(){
		return SHOW_BEST_TYPE.WEEKLY;
	}
	
	public SHOW_BEST_TYPE getAll(){
		return SHOW_BEST_TYPE.ALL;
	}
	
	@Property
	private int findNumber;
	
	@InjectPage
	private ShowBestVideos showBestVideos;
	
	void setupRender() {
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
	
	Object onSuccess(){
		showBestVideos.setType(this.findType);
		showBestVideos.setCount(this.findNumber);
		return showBestVideos;

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
