package es.udc.acarballal.elmas.model.adminservice.util;

import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class EncodeProcess extends Thread{
	
	private String file;
	private Long userId;
	private String title;
	private String comment;
	private UserService userService;
	private VideoService videoService;
	
	public EncodeProcess(String file, Long userId, String title, 
			String comment, UserService userService, 
			VideoService videoService){
		
		this.file = file;
		this.userId = userId;
		this.title = title;
		this.comment = comment;
		this.userService = userService;
		this.videoService =  videoService;
	}

	public void run(){
		TaskCentral central = TaskCentral.instance();
//		UploadTaskList taskList = UploadTaskList.instance();
		UploadTask task = new UploadTask(userId, title, comment, file, 
				userService, videoService);
		central.addProcess(task);
//		taskList.addProcess(task);		
	}
}
