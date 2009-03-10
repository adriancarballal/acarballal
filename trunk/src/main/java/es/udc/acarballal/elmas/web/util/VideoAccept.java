package es.udc.acarballal.elmas.web.util;

import java.util.Calendar;

import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.ConfigurationParametersManager;
import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.MissingConfigurationParameterException;
import es.udc.acarballal.elmas.ffmpeg.process.IProcess;
import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class VideoAccept implements IProcess{

	private static final String URL_CONTAINER_PARAMETER = "container/url";
	private static String containerURL;
	
	static{
		try {
			containerURL = 
				ConfigurationParametersManager.getParameter(URL_CONTAINER_PARAMETER);
		} catch (MissingConfigurationParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Long userId;
	private String title;
	private String comment;
	private String originalFile;
	private String flvFile;
	private String rtFile;
	private String snapshot;
	private String autogenerated;
	
	private VideoService videoService;
	
	public void setUserService(VideoService videoService) {
		this.videoService = videoService;
	}
	
	public VideoAccept(Long userId, String title, String comment, 
			String originalFile, String folder){
		this.userId = userId;
		this.title = title;
		this.comment = comment;
		this.originalFile = originalFile;
		
		this.autogenerated = folder.substring(folder.lastIndexOf("\\")+1);
		String url = containerURL + autogenerated + "/";
		
		this.flvFile = url + autogenerated + ".flv";
		this.rtFile = url + autogenerated + ".3gp";
		this.snapshot = url + autogenerated + ".jpg";
	}
	
	public boolean execute() {
		try {
			videoService.addVideo(userId, title, comment, snapshot, originalFile, 
					flvFile, rtFile, Calendar.getInstance());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void undo(){}

}
