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
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;

@AuthenticationPolicy(AuthenticationPolicyType.PARTICIPANTS)
public class AboutMe {
	
	private static final int COUNT = 7;
	
	@InjectComponent
	private Zone comments;
	
	@Inject
	private Locale locale;
	
	@Persist
	private int startIndex;
	
	@SuppressWarnings("unused")
	@Property
	private UserComment userComment;
	
	private UserCommentBlock userCommentBlock;
	
	@Inject
	private UserService userService;
	
	@ApplicationState
	private UserSession userSession;
	
	private void fill(){
		userCommentBlock = 
			userService.findUserCommentsByCommented(userSession.getUserProfileId(), 
					startIndex, COUNT);
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
	
	public List<UserComment> getUserComments(){
		return userCommentBlock.getUserComments();
	}
	
	void onActivate() {
		//startIndex=0;
		fill();
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
	
		
}
