package es.udc.acarballal.elmas.web.pages.user;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.usercomment.UserComment;
import es.udc.acarballal.elmas.model.userservice.UserCommentBlock;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.util.UserSession;

public class AboutMe {
	
	private int startIndex = 0;
	private int count = 4;
	private UserCommentBlock userCommentBlock;
	
	@SuppressWarnings("unused")
	@Property
	private UserComment userComment;
	
	@ApplicationState
	private UserSession userSession;
	
	@Inject
	private UserService userService;
	
	@Inject
	private Locale locale;
	
	public List<UserComment> getUserComments(){
		return userCommentBlock.getUserComments();
	}

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-count >= 0) {
			return new Object[] {startIndex-count, count};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {
		
		if (userCommentBlock.getExistMoreUserComments()) {
			return new Object[] {startIndex+count, count};
		} else {
			return null;
		}
		
	}
	
	Object[] onPassivate() {
		return new Object[] {startIndex, count};
	}
	
	void onActivate(int startIndex, int count) {
		this.startIndex = startIndex;
		this.count = count;
		userCommentBlock = 
			userService.findUserCommentsByCommented(userSession.getUserProfileId(), 
					startIndex, count);
	}
	
}
