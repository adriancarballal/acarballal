package es.udc.acarballal.elmas.web.util;

import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class VideoPostProcessing extends Thread{
	
	private String file;
	private Long userId;
	private String title;
	private String comment;
	private VideoService videoService;
	private UserService userService;
	
	public VideoPostProcessing(String file, Long userId, String title, 
			String comment, VideoService videoService, UserService userService){
		this.file = file;
		this.userId = userId;
		this.title = title;
		this.comment = comment;
		this.videoService = videoService;
		this.userService = userService;
	}

	public void run(){
		UploadTaskList taskList = UploadTaskList.instance();
		UploadTask task = new UploadTask(userId, title, comment, file, videoService, userService);
		taskList.addProcess(task);		
	}
}
