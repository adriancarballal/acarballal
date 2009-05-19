package es.udc.acarballal.elmas.web.pages.user;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.exceptions.InvalidOperationException;
import es.udc.acarballal.elmas.model.exceptions.VideoAlreadyVotedException;
import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.model.vote.Vote.VOTE_TYPES;
import es.udc.acarballal.elmas.web.pages.errors.AlreadyVotedVideo;
import es.udc.acarballal.elmas.web.pages.errors.InstanceNotFound;
import es.udc.acarballal.elmas.web.pages.errors.InvalidOperation;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicy;
import es.udc.acarballal.elmas.web.services.AuthenticationPolicyType;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class Vote {
	
	private static final int PRESELECTED_VIDEO_WINDOW = 100;
	
	@Inject
	private Block alreadyComplaint;
	
	@SuppressWarnings("unused")
	@InjectComponent
	private Zone complaintZone;
	
	@Inject
	private Block alreadyFavourite;
	
	@SuppressWarnings("unused")
	@InjectComponent
	private Zone favouriteZone;
	
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
	
	@OnEvent(component="complaint")
	Object complaintVideo(Long complaintedVideo){
		try {
			videoService.complaintOfVideo(complaintedVideo, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		}
		return alreadyComplaint;
	}
	
	@OnEvent(component="addToFavourite")
	Object addToFavourite(Long videoId){
		try {
			videoService.addToFavourites(userSession.getUserProfileId(), videoId);
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (DuplicateInstanceException e) {
			return InvalidOperation.class;
		}
		return alreadyFavourite;
	}

	public VOTE_TYPES getBad(){
		return VOTE_TYPES.BAD;
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
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
	
	public boolean isNotComplainted(){
		return !videoService.isComplaintedBy(
				userSession.getUserProfileId(), video.getVideoId());
	}
	
	public boolean isNotFavourite(){
		return !videoService.isFavourite(
				userSession.getUserProfileId(), video.getVideoId());
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
	
	private Object voteVideo(VOTE_TYPES vote){
		try {
			videoService.voteVideo(vote, userSession.getUserProfileId(), video.getVideoId());
		} catch (InstanceNotFoundException e) {
			return InstanceNotFound.class;
		} catch (VideoAlreadyVotedException e) {
			return AlreadyVotedVideo.class;
		} catch (InvalidOperationException e) {
			return InvalidOperation.class;
		}
		return Vote.class;
	}
	
	@OnEvent(component="voteVeryBad")
	Object voteVeryBad(){
		return voteVideo(VOTE_TYPES.VERY_BAD);
	}
	
	@OnEvent(component="voteBad")
	Object voteBad(){
		return voteVideo(VOTE_TYPES.BAD);
	}
	
	@OnEvent(component="voteNormal")
	Object voteNormal(){
		return voteVideo(VOTE_TYPES.NORMAL);
	}
	
	@OnEvent(component="voteGood")
	Object voteGood(){
		return voteVideo(VOTE_TYPES.NORMAL);
	}
	
	@OnEvent(component="voteVeryGood")
	Object voteVeryGood(){
		return voteVideo(VOTE_TYPES.VERY_GOOD);
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
