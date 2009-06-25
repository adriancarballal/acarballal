package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoBlock;
import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class FindVideos {
	
	private static final int COUNT = 5;
	
	@Persist
	private String keys;
	
	@Inject
	private Locale locale;
	
	@Persist("client")
	private int startIndex;
	
	@SuppressWarnings("unused")
	@Property
	private Video video;
	
	private VideoBlock videoBlock;
	
	@Inject
	private VideoService videoService;
	
	@InjectComponent
	private Zone videoZone;
	
	private void fill(){
		videoBlock =  videoService.findVideosByTitle(keys, startIndex, COUNT);
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}

	public Boolean getNextLinkContext() {
		if (videoBlock.getExistMoreVideos()) 
			return true;
		return false;
	}
	
	public Boolean getPreviousLinkContext() {
		if (startIndex-COUNT >= 0) 
			return true;
		return false;
	}
	
	public List<Video> getVideos() {
		return videoBlock.getVideos();
	}
	
	void setupRender(){
		startIndex=0;
	}
	
	void onActivate(String keys) {
		this.keys = keys;
		fill();
	}
	
	Object[] onPassivate() {
		return new Object[] {keys};
	}
	
	@OnEvent(component="next")
	Object onShowNext(){
		this.startIndex = this.startIndex + COUNT;
		fill();
		return videoZone.getBody();
	}

	@OnEvent(component="previous")
	Object onShowPrevious(){
		this.startIndex = this.startIndex - COUNT;
		fill();
		return videoZone.getBody();
	}
	
	public void setKeys(String keys){
		this.keys = keys;
	}
}
