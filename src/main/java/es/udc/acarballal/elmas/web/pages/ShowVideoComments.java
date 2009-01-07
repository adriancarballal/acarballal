package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.userprofile.UserProfile.Privileges_TYPES;
import es.udc.acarballal.elmas.model.userservice.UserProfileDetails;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.videocomment.VideoComment;
import es.udc.acarballal.elmas.model.videoservice.VideoCommentBlock;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ShowVideoComments {
	
	private Long videoId;	
	private int startIndex = 0;
	private int count = 4;
	private VideoCommentBlock videoCommentBlock;
	private VideoComment videoComment;
	
	private Privileges_TYPES privileges = Privileges_TYPES.NONE;
	
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
	
	public VideoComment getVideoComment() {
		return videoComment;
	}

	public void setVideoComment(VideoComment videoComment) {
		this.videoComment = videoComment;
	}

	public List<VideoComment> getVideoComments() {
		return videoCommentBlock.getUserComments();
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	@SuppressWarnings("unused")
	@Property
	 private String comment;
	
	@SuppressWarnings("unused")
	@Component
	private Form commentForm;
	
	@SuppressWarnings("unused")
	@Component(id = "comment")
	 private TextArea commentField;
	
	void onValidateForm() {

		if (!commentForm.isValid()) {
			return;
		}
		try {
			videoService.commentVideo(userSession.getUserProfileId(), 
					videoId, comment, Calendar.getInstance());
		} catch (InstanceNotFoundException e) {
			System.out.println("--> ERROR 1");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InsufficientPrivilegesException e) {
			System.out.println("--> ERROR 2");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidOperationException e) {
			System.out.println("--> ERROR 3");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			return;
		}

	}
	Object onSuccess(){
	   	
        return this;
    }
	
	void onActivate(Long videoId, int startIndex, int count){
		
		this.videoId = videoId;
		this.startIndex = startIndex;
		this.count = count;
		videoCommentBlock = 
			videoService.findVideoCommentsByVideoId(videoId, startIndex, count);
	}

	Object[] onPassivate() {
		return new Object[] {videoId, startIndex, count};
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-count >= 0) {
			return new Object[] {videoId, startIndex-count, count};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {
		
		if (videoCommentBlock.getExistMoreUserComments()) {
			return new Object[] {videoId, startIndex+count, count};
		} else {
			return null;
		}
		
	}
	
	public boolean getDeletable(){
		if (!userSessionExists) return false;
		if (userSession.getPrivileges()==Privileges_TYPES.ADMIN || 
			userSession.getUserProfileId().equals(
					videoComment.getCommentator().getUserProfileId()))
			return true;
		return false;
	}

}
