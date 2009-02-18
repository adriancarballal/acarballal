package es.udc.acarballal.elmas.web.util;

import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class VideoPostProcessing extends Thread{
	
	private String file;
	private Long userId;
	private String title;
	private String comment;
	private VideoService videoService;
	
	public VideoPostProcessing(String file, Long userId, String title, 
			String comment, VideoService videoService){
		this.file = file;
		this.userId = userId;
		this.title = title;
		this.comment = comment;
		this.videoService = videoService;
	}

	public void run(){
		UploadTaskList taskList = UploadTaskList.instance();
		UploadTask task = new UploadTask(userId, title, comment, file, videoService);
		taskList.addProcess(task);		
	}
}
