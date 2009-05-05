package es.udc.acarballal.elmas.model.adminservice.thread;

import es.udc.acarballal.elmas.model.adminservice.util.UploadTask;

public class TaskThread extends Thread{
	
	private ThreadList parentList;
	private UploadTask uploadTask;
	
	public TaskThread(UploadTask uploadTask){
		this.uploadTask = uploadTask;
	}
	
	public void setParentList(ThreadList tl){
		this.parentList = tl;
	}

	public void run(){
		uploadTask.execute();
		parentList.setOffUse();
		if(!parentList.isEmpty()) parentList.run();
	}

}
