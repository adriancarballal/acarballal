package es.udc.acarballal.elmas.web.pages.admin;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videocomplaint.VideoComplaint;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ADMINISTRATORS)
public class ShowVideoComplaint {

	@Inject
	private AdminService adminService;
	
	@InjectComponent
	private Zone complaint;
	
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
	private Video video;
	
	@Property
	private VideoComplaint videoComplaint;
	
	@Inject
	private VideoService videoService;
		
	@OnEvent(component="deleteComplaint")
	Object deleteComplaint(Long videoComplaintId){
		try {
			adminService.deleteVideoComplaint(
					videoComplaintId, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		fill();
		return complaint;
	}

	@OnEvent(component="deleteVideo")
	Object deleteVideo(Long videoId){
		try {
			videoService.deleteVideo(videoId, userSession.getUserProfileId());
			
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		fill();
		return complaint;
	}
	
	@OnEvent(component="deleteUser")
	Object deleteUser(Long userProfileId){
		try {
			adminService.deleteUserProfile(userProfileId, userSession.getUserProfileId());
			
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		fill();
		return complaint;
	}
	
	private void fill(){
		videoComplaint = null;
		video=null;
		try {
			videoComplaint = adminService.findFirstVideoComplaints(userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			//NOT IMPLEMENTED
		} catch (InsufficientPrivilegesException e) {
			//NOT IMPLEMENTED
		}
		if(videoComplaint!=null) video = videoComplaint.getReference();
		
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	public boolean getVideoComplaintExists(){
		return videoComplaint==null;
	}
	
	void onActivate(){
		fill();
	}
}
