package es.udc.acarballal.elmas.web.pages.user;

import java.text.DateFormat;
import java.util.Calendar;
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
import es.udc.acarballal.elmas.web.pages.errors.AlreadyVotedVideo;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InsufficientPrivileges;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class Vote {
	
	private static final int PRESELECTED_VIDEO_WINDOW = 100;
	
	@Property
	private VOTE_TYPES vote = VOTE_TYPES.NORMAL;
	
	@Property
	private Calendar endDate;
	
	@Property
	private Calendar startDate;
	
	@SuppressWarnings("unused")
	@Property
	private int remainingVotes;

	@Persist
	private Long videoId;
	
	@Property
	@Persist
	private Video video;
	
	
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
	
	@Inject
	private Locale locale;
	
	@Inject
	private VideoService videoService;
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}

	public boolean getIsVotable(){
		try {
			return userSessionExists && 
			!videoService.isVideoVotable(videoId, userSession.getUserProfileId())
			&& (videoService.getNumberVotesRemaining(userSession.getUserProfileId())>0);
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}
	
	public boolean getDoVoting(){
		try {
			return videoService.getNumberVotesRemaining(userSession.getUserProfileId())>0;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}
	
	@SuppressWarnings("unused")
	@Component
	private Form voteForm;

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
	
	void setupRender() throws InstanceNotFoundException{
		startDate = Calendar.getInstance();
		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		startDate.set(Calendar.HOUR, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.AM_PM, Calendar.AM);
		startDate.set(Calendar.MILLISECOND, 0);
		endDate = Calendar.getInstance();
		endDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		endDate.set(Calendar.HOUR, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
		endDate.set(Calendar.AM_PM, Calendar.PM);
		endDate.set(Calendar.MILLISECOND, 999);
		if(!getDoVoting()) return;
		video = 
			videoService.findRandomVotableVideo(
					userSession.getUserProfileId(), PRESELECTED_VIDEO_WINDOW);
		videoId = video.getVideoId();
		remainingVotes = videoService.getNumberVotesRemaining(userSession.getUserProfileId());

	}
		
	Object onSuccess(){
		try {
			videoService.voteVideo(vote, userSession.getUserProfileId(), video.getVideoId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (InsufficientPrivilegesException e) {
			return InsufficientPrivileges.class;
		} catch (VideoAlreadyVotedException e) {
			return AlreadyVotedVideo.class;
		}
        return Vote.class;
    }



}
