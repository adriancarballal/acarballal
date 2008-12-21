package es.udc.acarballal.elmas.web.pages.user;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.util.UserSession;
import es.udc.acarballal.elmas.web.util.VideoGridDataSource;

public class MyVideos {
	
	private final static int ROWS_PER_PAGE = 5;
	
	private VideoGridDataSource videoGridDataSource;
	private Video video;
	
	@ApplicationState
	private UserSession userSession;

	@Inject
	private Locale locale;
	
	@Inject
	private VideoService videoService;
	
	public VideoGridDataSource getVideoGridDataSource() {
		return videoGridDataSource;
	}
	
	public Video getVideo() {
		return video;
	}
	
	public void setVideo(Video video) {
		this.video = video;
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}
	
	public int getRowsPerPage() {
		return ROWS_PER_PAGE;
	}
	
	void onActivate() 
		throws ParseException {
		
		videoGridDataSource = 
			new VideoGridDataSource(videoService, userSession.getUserProfileId());
		
	}
	
	Object[] onPassivate() {
		return new Object[] {};
	}
		
}
