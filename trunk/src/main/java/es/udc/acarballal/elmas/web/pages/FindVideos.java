package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoBlock;
import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class FindVideos {
	
	private int startIndex = 0;
	private int count = 3;
	
	private VideoBlock videoBlock;
	
	private String keys;
	
	@SuppressWarnings("unused")
	@Property
	private Video video;
	
	@Inject
	private VideoService videoService;
	
	@Inject
	private Locale locale;
	
	@SuppressWarnings("unused")
	@Inject
	@Path("context:/logo/logo.jpg")
	@Property
	private Asset flag;
	
	public void setKeys(String keys){
		this.keys = keys;
	}
	
	public List<Video> getVideos() {
		return videoBlock.getVideos();
	}

	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-count >= 0) {
			return new Object[] {keys, startIndex-count, count};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {
		
		if (videoBlock.getExistMoreVideos()) {
			return new Object[] {keys, startIndex+count, count};
		} else {
			return null;
		}
		
	}
	
	Object[] onPassivate() {
		return new Object[] {keys, startIndex, count};
	}
	
	void onActivate(String keys, int startIndex, int count) {
		this.startIndex = startIndex;
		this.count = count;
		this.keys = keys;
		videoBlock =  videoService.findVideosByTitle(keys, startIndex, count);
	}

}
