package es.udc.acarballal.elmas.model.adminservice.util;

import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.acarballal.elmas.model.videoservice.VideoService;

public class EncodeProcess extends Thread{
	
	private String file;
	private Long userId;
	private String title;
	private String comment;
	private AdminService adminService;
	private VideoService videoService;
	
	public EncodeProcess(String file, Long userId, String title, 
			String comment, AdminService adminService, 
			VideoService videoService){
		
		this.file = file;
		this.userId = userId;
		this.title = title;
		this.comment = comment;
		this.adminService = adminService;	
	}

	public void run(){
		UploadTaskList taskList = UploadTaskList.instance();
		UploadTask task = new UploadTask(userId, title, comment, file, 
				adminService, videoService);
		taskList.addProcess(task);		
	}
}
