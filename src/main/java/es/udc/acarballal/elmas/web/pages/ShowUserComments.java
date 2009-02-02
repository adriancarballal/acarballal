package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.usercomment.UserComment;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.UserCommentBlock;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ShowUserComments {
	
	private static final int COUNT = 4;	
	
	@SuppressWarnings("unused")
	@Property
	private String comment;
	
	@SuppressWarnings("unused")
	@Component(id = "comment")
	private TextArea commentField;
	
	@SuppressWarnings("unused")
	@Component
	private Form commentForm;
	
	@InjectComponent
	private Zone comments;
	
	@Inject
	private Locale locale;
	
	@Inject
	private Messages messages;
	
	@Persist
	private int startIndex;
	
	@SuppressWarnings("unused")
	@Property
	private UserComment userComment;
	
	private UserCommentBlock userCommentBlock;
	
	private Long userId;
	
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
		userCommentBlock =
			userService.findUserCommentsByCommented(userId, startIndex, COUNT);
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	public Boolean getNextLinkContext() {
		
		if (userCommentBlock.getExistMoreUserComments()) 
			return true;
		return false;
		
	}
	
	public Boolean getPreviousLinkContext() {
		
		if (startIndex-COUNT >= 0) 
			return true;
		return false;
	}
	
	public boolean getDeletable(){
		if (!userSessionExists) return false;
		if (userSession.getPrivileges()==Privileges_TYPES.ADMIN || 
			userSession.getUserProfileId().equals(
					userComment.getCommentator().getUserProfileId()))
			return true;
		return false;
	}
	
	public List<UserComment> getUserComments() {
		return userCommentBlock.getUserComments();
	}

	void onActivate(Long userId){
		this.userId = userId;
		fill();		
	}
	
	Object[] onPassivate() {
		return new Object[] {userId};
	}
	
	@OnEvent(component="next")
	Object onShowNext(){
		this.startIndex = this.startIndex + COUNT;
		fill();
		return comments.getBody();
	}
	
	@OnEvent(component="previous")
	Object onShowPrevious(){
		this.startIndex = this.startIndex - COUNT;
		fill();
		return comments.getBody();
	}
	
	Object onSuccess(){
		this.startIndex = 0;
		fill();
		comment="";
		return comments;
    }
	
	void onValidateForm() {

		if (!commentForm.isValid()) {
			return;
		}
		try {
			userService.commentUser(userId, userSession.getUserProfileId(),
					comment, Calendar.getInstance());
		} catch (InstanceNotFoundException e) {
			commentForm.recordError(messages.get("error-instancenotfound"));
		} catch (InsufficientPrivilegesException e) {
			commentForm.recordError(messages.get("error-insufficientPrivileges"));
		} catch (InvalidOperationException e) {
			commentForm.recordError(messages.get("error-invalidOperation"));
		} catch (Exception e){
			return;
		}
	}
	
	@OnEvent(component="deleteComment")
	Object deleteComment(Long commentId){
		try {
			userService.deleteUserComment(commentId, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		fill();
		return comments;
	}
	
	@OnEvent(component="complaintComment")
	Object complaintComment(Long userCommentId){
		try {
			userService.complaintUserComment(userCommentId, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		fill();
		return comments;
	}

}
