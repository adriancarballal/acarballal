package es.udc.acarballal.elmas.web.pages.admin;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaint;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.util.UserSession;

public class ShowVideoComplaint {

	private VideoComplaint videoComplaint;
	private Video video;
	
	@SuppressWarnings("unused")
	@Property
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
	
	@Inject
	private VideoService videoService;
	
	@Inject
	private Locale locale;
	
	public Video getVideo(){
		return video;
	}
	
	public VideoComplaint getVideoComplaint(){
		return videoComplaint;
	}
	
	public boolean getVideoComplaintExists(){
		return videoComplaint==null;
	}

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	void onActivate(){
		
		videoComplaint = videoService.findFirstVideoComplaints();
		video = videoComplaint.getReference();
	}
	
}
