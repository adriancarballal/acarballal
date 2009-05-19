package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videocomment.VideoComment;
import es.udc.acarballal.elmas.model.videoservice.VideoCommentBlock;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ShowVideoComments {
	
	private static final int COUNT = 6;
	
	@SuppressWarnings("unused")
	@Property
	private String comment;
	
	@SuppressWarnings("unused")
	@Component(id = "comment")
	private TextArea commentField;
	
	@SuppressWarnings("unused")
	@Component
	private Form commentForm;
	
	@InjectComponent
	private Zone comments;
	
	@Inject
	private Block alreadyComplaint;
	
	@SuppressWarnings("unused")
	@InjectComponent
	private Zone complaintZone;
	
	@Inject
	private Locale locale;
	
	@Inject
	private Messages messages;
	
	@Persist
	private int startIndex;
	
	@SuppressWarnings("unused")
	@Property
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
	
	@SuppressWarnings("unused")
	@Property
	private VideoComment videoComment;
	
	private VideoCommentBlock videoCommentBlock;
	
	private Long videoId;
	private Video video;
	
	@Inject
	private VideoService videoService;
	
	private void fill(){
		videoCommentBlock = 
			videoService.findVideoCommentsByVideoId(videoId, startIndex, COUNT);	
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	public boolean getDeletable(){
		if (!userSessionExists) return false;
		if (userSession.getPrivileges()==Privileges_TYPES.ADMIN || 
			userSession.getUserProfileId().equals(
					videoComment.getCommentator().getUserProfileId()))
			return true;
		return false;
	}
	
	public Boolean getNextLinkContext() {
		if (videoCommentBlock.getExistMoreUserComments()) 
			return true;
		return false;
	}
	
	public Boolean getPreviousLinkContext() {
		if (startIndex-COUNT >= 0) 
			return true;
		return false;
	}

	public List<VideoComment> getVideoComments() {
		return videoCommentBlock.getUserComments();
	}
	
	void onActivate(Long videoId){
		this.videoId = videoId;
		try {
			video = videoService.findVideoById(videoId);
		} catch (InstanceNotFoundException e) {
			//TODO
			//MAYBE BUG
		}
		fill();
	}
	
	Object[] onPassivate() {
		return new Object[] {videoId};
	}
	
	public boolean isNotMyself(){
		if(!userSessionExists) return true;
		return !video.getUserProfile().getUserProfileId()
			.equals(userSession.getUserProfileId());
	}
	
	public boolean isNotComplainted(){
		if(!userSessionExists) return false;
		return  !videoService.isVideoCommentComplaintedBy(
				userSession.getUserProfileId(), 
				videoComment.getCommentId());
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
	
	Object onSuccess(){
		fill();
        return comments;
    }
	
	void onValidateForm() {

		if (!commentForm.isValid()) {
			return;
		}
		try {
			videoService.commentVideo(userSession.getUserProfileId(), 
					videoId, comment, Calendar.getInstance());
		} catch (InstanceNotFoundException e) {
			commentForm.recordError(messages.get("error-instancenotfound"));
		} catch (InvalidOperationException e) {
			commentForm.recordError(messages.get("error-invalidOperation"));
		} catch (Exception e){
			return;
		}
	}
	
	@OnEvent(component="deleteComment")
	Object deleteComment(Long commentId){
		try {
			videoService.deleteVideoComment(commentId, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		}
		if(this.videoCommentBlock.getUserComments().size()==1 && (startIndex-COUNT >= 0)){
			startIndex = startIndex - COUNT;
		}

		fill();
		return comments;
	}
	
	@OnEvent(component="complaintComment")
	Object complaintComment(Long videoCommentId){
		try {
			videoService.complaintOfVideoComment(videoCommentId, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		}
		fill();
		return alreadyComplaint;
	}

}
