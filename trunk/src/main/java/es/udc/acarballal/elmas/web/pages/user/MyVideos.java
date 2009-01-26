package es.udc.acarballal.elmas.web.pages.user;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoBlock;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.acarballal.elmas.web.util.UserSession;

public class MyVideos {
	
	private int count = 3;
	@SuppressWarnings("unused")
	@Inject
	@Path("context:/logo/logo.jpg")
	@Property
	private Asset flag;
	@Inject
	private Locale locale;
	
	private int startIndex = 0;
	
	@ApplicationState
	private UserSession userSession;
	
	@SuppressWarnings("unused")
	@Property
	private Video video;
	
	private VideoBlock videoBlock;
	
	@Inject
	private VideoService videoService;
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}

	public Object[] getNextLinkContext() {
		
		if (videoBlock.getExistMoreVideos()) {
			return new Object[] {startIndex+count, count};
		} else {
			return null;
		}
		
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-count >= 0) {
			return new Object[] {startIndex-count, count};
		} else {
			return null;
		}
		
	}
	
	public List<Video> getVideos() {
		return videoBlock.getVideos();
	}
	
	void onActivate(int startIndex, int count) {
		this.startIndex = startIndex;
		this.count = count;
		videoBlock = videoService.findVideosByUser(userSession.getUserProfileId(), 
				startIndex, count);
	}
	
	Object[] onPassivate() {
		return new Object[] {startIndex, count};
	}
	
}

