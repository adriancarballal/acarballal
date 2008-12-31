package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ShowVideo {

	private Video video;
	private boolean foundVideo;
	
	@Inject
	private VideoService videoService;
	
	@Inject
	private Locale locale;
	
	public Video getVideo(){
		 return this.video;
	}
	
	public boolean getFoundVideo(){
		return !foundVideo;
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	 
	void onActivate(Long id){
		try {
			video = videoService.findVideoById(id);
			foundVideo = true;
		} catch (InstanceNotFoundException e) {
			foundVideo = false;
		}
	}
	
}
