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
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoBlock;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class MyFavourites {
	
private static final int COUNT = 5;
	
	@Inject
	private Locale locale;
	
	@Persist
	private int startIndex;
	
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private Video video;
	
	private VideoBlock videoBlock;
	
	@Inject
	private VideoService videoService;
	
	@InjectComponent
	private Zone videoZone;
	
	private void fill(){
		try {
			videoBlock = videoService.findFavourites(userSession.getUserProfileId(), startIndex, COUNT);
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR1");
			e.printStackTrace();
		} catch (InsufficientPrivilegesException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR2");
			e.printStackTrace();
		}		
	}

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	public Boolean getNextLinkContext() {
		
		if (videoBlock.getExistMoreVideos()) 
			return true;
		return false;
		
	}
	
	public Boolean getPreviousLinkContext() {
		
		if (startIndex-COUNT >= 0) 
			return true;
		return false;
		
	}
	
	public List<Video> getVideos() {
		return videoBlock.getVideos();
	}
	
	void onActivate() {
		startIndex=0;
		fill();
	}
	
	@OnEvent(component="next")
	Object onShowNext(){
		this.startIndex = this.startIndex + COUNT;
		fill();
		return videoZone.getBody();
	}
	
	@OnEvent(component="previous")
	Object onShowPrevious(){
		this.startIndex = this.startIndex - COUNT;
		fill();
		return videoZone.getBody();
	}

	@OnEvent(component="deleteFromFavourite")
	Object onDeleteElement(Long videoId){
		try {
			videoService.removeFromFavourites(userSession.getUserProfileId(), videoId);
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		if(videoBlock.getVideos().size()==1 && (startIndex-COUNT >= 0)){
			startIndex = startIndex - COUNT;
		}
		fill();
		return videoZone.getBody();
	}

}
