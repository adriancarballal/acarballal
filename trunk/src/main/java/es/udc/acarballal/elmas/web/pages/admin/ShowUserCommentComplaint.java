package es.udc.acarballal.elmas.web.pages.admin;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.acarballal.elmas.model.adminservice.UserCommentComplaintBlock;
import es.udc.acarballal.elmas.model.usercommentcomplaint.UserCommentComplaint;
import es.udc.acarballal.elmas.web.util.UserSession;

public class ShowUserCommentComplaint {

	private int startIndex = 0;
	private int count = 4;
	private UserCommentComplaintBlock userCommentComplaintBlock;
	private UserCommentComplaint userCommentComplaint;
	
	@SuppressWarnings("unused")
	@Property
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
	
	@Inject
	private AdminService adminService;
	
	@Inject
	private Locale locale;
	
	public UserCommentComplaint getUserCommentComplaint(){
		return userCommentComplaint;
	}
	
	public void setUserCommentComplaint(UserCommentComplaint userCommentComplaint){
		this.userCommentComplaint = userCommentComplaint;
	}
	
	public List<UserCommentComplaint> getUserCommentComplaints(){
		return userCommentComplaintBlock.getUserCommentComplaints();
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	void onActivate(int startIndex, int count){
		this.startIndex = startIndex;
		this.count = count;
		userCommentComplaintBlock = adminService.findUserCommentComplaints(startIndex, count);
	}
	
	Object[] onPassivate() {
		return new Object[] {startIndex, count};
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-count >= 0) {
			return new Object[] {startIndex-count, count};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {
		
		if (userCommentComplaintBlock.getExistMoreUserCommentComplaints()) {
			return new Object[] {startIndex+count, count};
		} else {
			return null;
		}
		
	}
}
