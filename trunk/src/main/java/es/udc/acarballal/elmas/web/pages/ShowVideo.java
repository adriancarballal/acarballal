package es.udc.acarballal.elmas.web.pages;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.acarballal.elmas.model.video.Video;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ShowVideo {

	private Long videoId;
	private Video video;
	private boolean foundVideo;
	
	private int startIndex = 0;
	private int count = 4;
	
	@Inject
	private VideoService videoService;
	
	@Inject
	private Locale locale;
	
	public Video getVideo(){
		 return this.video;
	}
	
	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public boolean getFoundVideo(){
		return !foundVideo;
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	
	void onActivate(Long videoId){
		try {
			this.videoId = videoId;
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


