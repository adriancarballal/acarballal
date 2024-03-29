package es.udc.acarballal.elmas.web.pages.mobile.user;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.exceptions.VideoAlreadyVotedException;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.pages.mobile.errors.AlreadyVotedVideo;
import es.udc.acarballal.elmas.web.pages.mobile.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.mobile.errors.InvalidOperation;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class Vote {
		
	private static final int PRESELECTED_VIDEO_WINDOW = 100;
	public enum VOTE_TYPES {BAD, GOOD, NORMAL, VERY_BAD, VERY_GOOD}
	
	@Property
	private Calendar endDate;
		
	private boolean founded = true;
		
	@Inject
	private Locale locale;
		
	@SuppressWarnings("unused")
	@Property
	private int remainingVotes;

	@Property
	private Calendar startDate;
		
	@ApplicationState
	private UserSession userSession;
		
	@SuppressWarnings("unused")
	@Property
	private boolean userSessionExists;
		
	@Property
	@Persist
	private Video video;
		
	@Persist
	private Long videoId;
		
	@Inject
	private VideoService videoService;
		
	@Property
	private VOTE_TYPES vote = VOTE_TYPES.NORMAL;
		
	@SuppressWarnings("unused")
	@Component
	private Form voteForm;
		
	public VOTE_TYPES getBad(){
		return VOTE_TYPES.BAD;
	}
		
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}
		
	public boolean getDoVoting(){
		try {
			return founded && videoService.getNumberVotesRemaining(userSession.getUserProfileId())>0;
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}

	public VOTE_TYPES getGood(){
		return VOTE_TYPES.GOOD;
	}
		
	public boolean getIsVotable(){
		try {
			return founded && userSessionExists && 
			!videoService.isVideoVotable(videoId, userSession.getUserProfileId())
			&& (videoService.getNumberVotesRemaining(userSession.getUserProfileId())>0);
		} catch (InstanceNotFoundException e) {
			return false;
		}
	}
		
	public VOTE_TYPES getNormal(){
		return VOTE_TYPES.NORMAL;
	}
		
	public VOTE_TYPES getVeryBad(){
		return VOTE_TYPES.VERY_BAD;
	}
		
	public VOTE_TYPES getVeryGood(){
		return VOTE_TYPES.VERY_GOOD;
	}
		
	Object onSuccess(){
		try {
			videoService.voteVideo(getVote(vote), userSession.getUserProfileId(), video.getVideoId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (VideoAlreadyVotedException e) {
			return AlreadyVotedVideo.class;
		} catch (InvalidOperationException e) {
			return InvalidOperation.class;
		}
        return Vote.class;
	}
	
	private short getVote(VOTE_TYPES vote){
		if(vote==VOTE_TYPES.VERY_GOOD) return 5;
		if(vote==VOTE_TYPES.GOOD) return 4;
		if(vote==VOTE_TYPES.NORMAL) return 3;
		if(vote==VOTE_TYPES.BAD) return 2;
		return 1;
	}
			
	void setupRender(){
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
		try {
			video = 
				videoService.findRandomVotableVideo(
						userSession.getUserProfileId(), PRESELECTED_VIDEO_WINDOW);
			videoId = video.getVideoId();
			remainingVotes = videoService.getNumberVotesRemaining(userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			founded=false;
			return;
		}
		
	}

}
