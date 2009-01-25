package es.udc.acarballal.elmas.web.pages.user;

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

import es.udc.acarballal.elmas.model.usercomment.UserComment;
import es.udc.acarballal.elmas.model.userservice.UserCommentBlock;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.util.UserSession;

public class MyUserComments {

	@Property
	@Persist
	private int startIndex;
	
	private int count;
	
	@Property
	@Persist
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
	
	@InjectComponent
	private Zone comments;
	
	public List<UserComment> getUserComments(){
		return userCommentBlock.getUserComments();
	}

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	@OnEvent(component="next")
	Object onShowNext(){
		this.startIndex = this.startIndex + count;
		fillData();
		return comments.getBody();
	}
	
	@OnEvent(component="previous")
	Object onShowPrevious(){
		this.startIndex = this.startIndex - count;
		fillData();
		return comments.getBody();
	}
	
	public Boolean getPreviousLinkContext() {
		
		if (startIndex-count >= 0) 
			return true;
		return false;
		
	}
	
	public Boolean getNextLinkContext() {
		
		if (userCommentBlock.getExistMoreUserComments()) 
			return true;
		return false;
		
	}
	
	void onActivate() {
		count=1;
		fillData();
	}
	
	private void fillData(){
		userCommentBlock = 
			userService.findUserCommentsByCommentator(userSession.getUserProfileId(),
					this.startIndex, this.count);
	}
}
