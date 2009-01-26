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
	
	private int count = 3;
	@SuppressWarnings("unused")
	@Inject
	@Path("context:/logo/logo.jpg")
	@Property
	private Asset flag;
	
	private String keys;
	
	@Inject
	private Locale locale;
	
	private int startIndex = 0;
	
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
			return new Object[] {keys, startIndex+count, count};
		} else {
			return null;
		}
		
	}

	public Object[] getPreviousLinkContext() {
		
		if (startIndex-count >= 0) {
			return new Object[] {keys, startIndex-count, count};
		} else {
			return null;
		}
		
	}
	
	public List<Video> getVideos() {
		return videoBlock.getVideos();
	}
	
	void onActivate(String keys, int startIndex, int count) {
		this.startIndex = startIndex;
		this.count = count;
		this.keys = keys;
		videoBlock =  videoService.findVideosByTitle(keys, startIndex, count);
	}
	
	Object[] onPassivate() {
		return new Object[] {keys, startIndex, count};
	}
	
	public void setKeys(String keys){
		this.keys = keys;
	}

}
