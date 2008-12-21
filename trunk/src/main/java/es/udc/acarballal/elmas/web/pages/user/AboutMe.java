package es.udc.acarballal.elmas.web.pages.user;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.usercomment.UserComment;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.util.CommentGridDataSource;
import es.udc.acarballal.elmas.web.util.UserSession;

public class AboutMe {

private final static int ROWS_PER_PAGE = 10;
	
	private CommentGridDataSource commentGridDataSource;
	private UserComment userComment;
	
	@ApplicationState
	private UserSession userSession;

	@Inject
	private Locale locale;
	
	@Inject
	private UserService userService;
	
	public CommentGridDataSource getCommentGridDataSource() {
		return commentGridDataSource;
	}
	
	public UserComment getUserComment() {
		return userComment;
	}
	
	public void setUserComment(UserComment userComment) {
		this.userComment = userComment;
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}
	
	public int getRowsPerPage() {
		return ROWS_PER_PAGE;
	}
	
	void onActivate() 
		throws ParseException {
		
		commentGridDataSource = 
			new CommentGridDataSource(userService, userSession.getUserProfileId());
		
	}
	
	Object[] onPassivate() {
		return new Object[] {};
	}

}
