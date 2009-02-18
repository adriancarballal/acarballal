package es.udc.acarballal.elmas.web.util;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import es.udc.acarballal.elmas.ffmpeg.process.IProcess;
import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class VideoAccept implements IProcess{

	private Long userId;
	private String title;
	private String comment;
	private String originalFile;
	private String flvFile;
	private String rtFile;
	private String snapshot;
	
	
	private VideoService videoService;
	
	public void setUserService(VideoService videoService) {
		this.videoService = videoService;
	}
	
	public VideoAccept(Long userId, String title, String comment, String originalFile){
		this.userId = userId;
		this.title = title;
		this.comment = comment;
		this.originalFile = originalFile;
		
		//TODO mediante ConfigurationParameters
		this.flvFile = "flvFileName";
		this.rtFile = "RealTimeFileName";
		this.snapshot = "snapshot";
	}
	
	public boolean execute() {
		try {
			videoService.addVideo(userId, title, comment, snapshot, originalFile, 
					flvFile, rtFile, Calendar.getInstance());
			System.out.println("Creado");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
