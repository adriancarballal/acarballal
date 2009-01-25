package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ShowVideo {

	private boolean foundVideo;
	private int startIndex = 0;
	private int count = 4;
	
	@Property
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
	
	public boolean getFoundVideo(){
		return !foundVideo;
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}

	void onActivate(Long videoId){
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
	
}


