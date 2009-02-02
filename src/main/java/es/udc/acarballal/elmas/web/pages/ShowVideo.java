package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ShowVideo {

	@Inject
	private Block alreadyComplaint;
	
	@SuppressWarnings("unused")
	@InjectComponent
	private Zone complaintZone;
	
	private boolean foundVideo;

	@Inject
	private Locale locale;
	
	@SuppressWarnings("unused")
	@Property
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
	
	@SuppressWarnings("unused")
	@Property
	@Persist
	private Video video;
	
	@Inject
	private VideoService videoService;
	
	@OnEvent(component="complaint")
	Object complaintVideo(Long complaintedVideo){
		try {
			videoService.complaintOfVideo(complaintedVideo, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		return alreadyComplaint;
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}

	public boolean getFoundVideo(){
		return !foundVideo;
	}
	
	void onActivate(Long videoId){
		try {
			video = videoService.findVideoById(videoId);
			foundVideo = true;
		} catch (InstanceNotFoundException e) {
			foundVideo = false;
		}
	}
	
}


