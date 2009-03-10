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

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.message.Message;
import es.udc.acarballal.elmas.model.userservice.MessageBlock;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class InBox {
	
private static final int COUNT = 5;
	
	@Inject
	private Locale locale;
	
	@Persist
	private int startIndex;
	
	@Inject
	private UserService userService;
	
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private Message message;
	
	private MessageBlock messageBlock;
	
	@InjectComponent
	private Zone messageZone;
	
	private void fill(){
		messageBlock = userService.findUserInBox(userSession.getUserProfileId(), 
				startIndex, COUNT);		
	}

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	public Boolean getNextLinkContext() {
		
		if (messageBlock.getExistMoreMessages()) 
			return true;
		return false;
		
	}
	
	public Boolean getPreviousLinkContext() {
		
		if (startIndex-COUNT >= 0) 
			return true;
		return false;
		
	}
	
	public List<Message> getMessages() {
		return messageBlock.getmessages();
	}
	
	void onActivate() {
		fill();
	}
	
	@OnEvent(component="next")
	Object onShowNext(){
		this.startIndex = this.startIndex + COUNT;
		fill();
		return messageZone.getBody();
	}
	
	@OnEvent(component="previous")
	Object onShowPrevious(){
		this.startIndex = this.startIndex - COUNT;
		fill();
		return messageZone.getBody();
	}
	
	@OnEvent(component="removeMessage")
	Object onRemoveVideo(Long messageId){
		try {
			userService.removeMessage(messageId, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		fill();
		return messageZone.getBody();
	}

}
