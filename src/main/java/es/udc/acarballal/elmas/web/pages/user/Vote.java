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
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class Vote {
	
	private static final int PRESELECTED_VIDEO_WINDOW = 100;
	private VOTE_TYPES vote = VOTE_TYPES.NORMAL;
	
	private Calendar endDate;
	private Calendar startDate;
	private int remainingVotes;

	@Persist
	private Long videoId;
	
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
	
	public Video getVideo(){
		return video;
	}
	
	public Calendar getEndDate() {
		return endDate;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public int getRemainingVotes() {
		return remainingVotes;
	}

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
	
	void setupRender(){
		startDate = Calendar.getInstance();
		endDate = (Calendar) startDate.clone();
		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		endDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		if(!getDoVoting()) return;
		try {
			video = 
				videoService.findRandomVotableVideo(
						userSession.getUserProfileId(), PRESELECTED_VIDEO_WINDOW);
			videoId = video.getVideoId();
			remainingVotes = videoService.getNumberVotesRemaining(userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			//System.out.println("NO VIDEO AVAILABLE:" + userSession.getFirstName());
			e.printStackTrace();
		
		}
	}
		
	Object onSuccess(){
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
        return Vote.class;
    }



}
