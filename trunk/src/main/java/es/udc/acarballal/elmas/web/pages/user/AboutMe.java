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
	
	private int count = 4;
	@Inject
	private Locale locale;
	private int startIndex = 0;
	
	@SuppressWarnings("unused")
	@Property
	private UserComment userComment;
	
	private UserCommentBlock userCommentBlock;
	
	@Inject
	private UserService userService;
	
	@ApplicationState
	private UserSession userSession;
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}

	public Object[] getNextLinkContext() {
		
		if (userCommentBlock.getExistMoreUserComments()) {
			return new Object[] {startIndex+count, count};
		} else {
			return null;
		}
		
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-count >= 0) {
			return new Object[] {startIndex-count, count};
		} else {
			return null;
		}
		
	}
	
	public List<UserComment> getUserComments(){
		return userCommentBlock.getUserComments();
	}
	
	void onActivate(int startIndex, int count) {
		this.startIndex = startIndex;
		this.count = count;
		userCommentBlock = 
			userService.findUserCommentsByCommented(userSession.getUserProfileId(), 
					startIndex, count);
	}
	
	Object[] onPassivate() {
		return new Object[] {startIndex, count};
	}
	
}
