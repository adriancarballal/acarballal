package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.acarballal.elmas.model.exceptions.VideoAlreadyVotedException;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.model.vote.Vote.VOTE_TYPES;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ShowVideo {

	private Long videoId;
	
	@Persist	//Solo lo utilizamos para el onValidateForm();
	private Video video;
	private boolean foundVideo;
	
	private VOTE_TYPES vote = VOTE_TYPES.NORMAL;
	
	private int startIndex = 0;
	private int count = 4;
	
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
	
	public void setVideoId(Long videoId){
		this.videoId = videoId;
	}
	
	public Video getVideo(){
		 return this.video;
	}
	
	public boolean getFoundVideo(){
		return !foundVideo;
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}

	void onActivate(Long videoId){
		this.videoId = videoId;
		System.out.println("ACTIVATE: " + this.videoId);
		try {
			video = videoService.findVideoById(videoId);
			foundVideo = true;
		} catch (InstanceNotFoundException e) {
			foundVideo = false;
		}
	}
	
	public Object[] getVideoContext() {
		return new Object[] {video.getVideoId(), startIndex, count};
	}

	public Object[] getUserContext() {
		return new Object[] {video.getUserProfile().getUserProfileId(), startIndex, count};
	}
	
	public boolean getIsVotable(){
		return userSessionExists && !videoService.isVideoVotable(videoId, userSession.getUserProfileId());
	}
	
	@SuppressWarnings("unused")
	@Component
	private Form voteForm;

	public VOTE_TYPES getVote() {
		return vote;
	}

	public void setVote(VOTE_TYPES vote) {
		this.vote = vote;
	}

	public VOTE_TYPES getVeryBad(){
		return VOTE_TYPES.VERY_BAD;
	}
	
	public VOTE_TYPES getBad(){
		return VOTE_TYPES.BAD;
	}
	
	public VOTE_TYPES getNormal(){
		return VOTE_TYPES.NORMAL;
	}
	
	public VOTE_TYPES getGood(){
		return VOTE_TYPES.GOOD;
	}
	
	public VOTE_TYPES getVeryGood(){
		return VOTE_TYPES.VERY_GOOD;
	}
	
	void onValidateForm(){
		try {
			videoService.voteVideo(vote, userSession.getUserProfileId(), video.getVideoId());
		} catch (InstanceNotFoundException e) {
			System.out.println("ERROR: 1");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InsufficientPrivilegesException e) {
			System.out.println("ERROR: 2");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VideoAlreadyVotedException e) {
			System.out.println("ERROR: 3");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Object onSuccess(){
		
        return Index.class;
    }
}


