package es.udc.acarballal.elmas.web.pages.admin;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.acarballal.elmas.model.adminservice.UserCommentComplaintBlock;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.usercommentcomplaint.UserCommentComplaint;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ADMINISTRATORS)
public class ShowUserCommentComplaint {

	private static final int COUNT = 7;
	
	@Inject
	private AdminService adminService;
	
	@InjectComponent
	private Zone complaints;
	
	@Inject
	private Locale locale;
	
	@Persist("client")
	private int startIndex;
	
	@SuppressWarnings("unused")
	@Property
	private UserCommentComplaint userCommentComplaint;
	
	private UserCommentComplaintBlock userCommentComplaintBlock;
	
	@Inject 
	private UserService userService;
	
	@SuppressWarnings("unused")
	@Property
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
	
	private void fill(){
		try {
			userCommentComplaintBlock = 
				adminService.findUserCommentComplaints(userSession.getUserProfileId(),
						startIndex, COUNT);
		} catch (InstanceNotFoundException e) {
			// NOT IMPLEMENTED
		} catch (InsufficientPrivilegesException e) {
			// NOT IMPLEMENTED
		}
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	public Boolean getNextLinkContext() {
		
		if (userCommentComplaintBlock.getExistMoreUserCommentComplaints()) 
			return true;
		return false;
		
	}
	
	public Boolean getPreviousLinkContext() {
		
		if (startIndex-COUNT >= 0) return true;
		return false;
		
	}
	
	public List<UserCommentComplaint> getUserCommentComplaints(){
		return userCommentComplaintBlock.getUserCommentComplaints();
	}
	
	void onActivate(){
		fill();
	}
	
	@OnEvent(component="deleteComment")
	Object onDeleteComment(Long id){
		try {
			userService.deleteUserComment(id, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		fill();
		return complaints;
	}
	
	@OnEvent(component="deleteComplaint")
	Object onDeleteComplaint(Long id){
		try {
			adminService.deleteUserCommentComplaint(
					id, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		fill();
		return complaints;
	}
	
	@OnEvent(component="next")
	Object onShowNext(){
		this.startIndex = this.startIndex + COUNT;
		fill();
		return complaints.getBody();
	}
	
	@OnEvent(component="previous")
	Object onShowPrevious(){
		this.startIndex = this.startIndex - COUNT;
		fill();
		return complaints.getBody();
	}
}
