package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.usercomment.UserComment;
import es.udc.acarballal.elmas.model.userservice.UserCommentBlock;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ShowUserComments {
	
	private Long userId;	
	private int startIndex = 0;
	private int count = 4;
	private UserCommentBlock userCommentBlock;
	
	@SuppressWarnings("unused")
	@Property
	private UserComment userComment;
	
	@SuppressWarnings("unused")
	@Property
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
	
	@Inject
	private UserService userService;
	
	@Inject
	private Locale locale;
	
	@SuppressWarnings("unused")
	@Property
	 private String comment;
	
	@SuppressWarnings("unused")
	@Component
	private Form commentForm;
	
	@Inject
	private Messages messages;
	
	@SuppressWarnings("unused")
	@Component(id = "comment")
	 private TextArea commentField;
	
	public List<UserComment> getUserComments() {
		return userCommentBlock.getUserComments();
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
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
			commentForm.recordError(messages.get("error-insufficientPrivileges"));
		} catch (Exception e){
			return;
		}

	}
	
	Object onSuccess(){
        return this;
    }
	
	void onActivate(Long userId, int startIndex, int count){
		
		this.userId = userId;
		this.startIndex = startIndex;
		this.count = count;
		userCommentBlock =
			userService.findUserCommentsByCommented(userId, startIndex, count);;
		
	}

	Object[] onPassivate() {
		return new Object[] {userId, startIndex, count};
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-count >= 0) {
			return new Object[] {userId, startIndex-count, count};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {
		
		if (userCommentBlock.getExistMoreUserComments()) {
			return new Object[] {userId, startIndex+count, count};
		} else {
			return null;
		}
		
	}

}
