package es.udc.acarballal.elmas.model.encoderservice.util;

import es.udc.acarballal.elmas.model.encoderservice.EncoderService;
import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class EncodeProcess extends Thread{
	
	private String file;
	private Long userId;
	private String title;
	private String comment;
	private VideoService videoService;
	private EncoderService encoderService;
	
	public EncodeProcess(String file, Long userId, String title, 
			String comment, VideoService videoService, 
			EncoderService encoderService){
		
		this.file = file;
		this.userId = userId;
		this.title = title;
		this.comment = comment;
		this.videoService = videoService;
		this.encoderService = encoderService;		
	}

	public void run(){
		UploadTaskList taskList = UploadTaskList.instance();
		UploadTask task = new UploadTask(userId, title, comment, file, 
				videoService, encoderService);
		taskList.addProcess(task);		
	}
}
