package es.udc.acarballal.elmas.web.pages.admin;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaint;
import es.udc.acarballal.elmas.web.util.UserSession;

public class ShowVideoComplaint {

	private VideoComplaint videoComplaint = null;
	private Video video = null;
	
	@SuppressWarnings("unused")
	@Property
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
	
	@Inject
	private AdminService adminService;
	
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
		
		videoComplaint = adminService.findFirstVideoComplaints();
		if(videoComplaint!=null) video = videoComplaint.getReference();
	}
	
}
