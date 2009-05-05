package es.udc.acarballal.elmas.model.adminservice.util;

import java.util.ArrayList;
import java.util.List;

public class UploadTaskList extends Thread{

	private List<UploadTask> taskList;
	
	public UploadTaskList(){
		taskList = new ArrayList<UploadTask>();
	}
	
	public void addProcess(UploadTask task){
		boolean inUse = size()>0;
		taskList.add(task);
		if(!inUse){
			this.start();
		}
	}
	
	public int size(){
		return taskList.size();
	}
	
	public void run(){
		if(!taskList.isEmpty()){
			taskList.get(0).execute();
			taskList.remove(0);
			run();
		}
	}
}
