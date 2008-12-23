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
	
	private int startIndex = 0;
	private int count = 3;
	private VideoBlock videoBlock;
	private Video video;
	
	@ApplicationState
	private UserSession userSession;
	
	@Inject
	private VideoService videoService;
	
	@Inject
	private Locale locale;
	
	@Inject
	@Path("context:/logo/logo.jpg")
	@Property
	private Asset flag;
	public Asset getFlag()	{
		//flag = new ExternalAsset("http://www.carlitospaez.com/images/logos/logo_claro.jpg", null);
		return flag;
	}	
	
	public List<Video> getVideos() {
		return videoBlock.getVideos();
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-count >= 0) {
			return new Object[] {startIndex-count, count};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {
		
		if (videoBlock.getExistMoreVideos()) {
			return new Object[] {startIndex+count, count};
		} else {
			return null;
		}
		
	}
	
	Object[] onPassivate() {
		return new Object[] {startIndex, count};
	}
	
	void onActivate(int startIndex, int count) {
		this.startIndex = startIndex;
		this.count = count;
		videoBlock = videoService.findVideosByUser(userSession.getUserProfileId(), 
				startIndex, count);
		//System.out.println("FLAG: " + flag.getResource().toURL());
	}
	
}

