package es.udc.acarballal.elmas.web.util;

import java.util.ArrayList;
import java.util.List;

public class UploadTaskList {

	private List<UploadTask> taskList;
	private static UploadTaskList instance;
	
	public static UploadTaskList instance() {
	    if (instance == null) instance = new UploadTaskList();
	    return instance;
	}
	
	private UploadTaskList(){
		taskList = new ArrayList<UploadTask>();
	}
	
	public void addProcess(UploadTask task){
		boolean inUse = taskListSize()>0;
		UploadTaskList.instance().taskList.add(task);
		if(!inUse) executeNextTask();
	}
	
	private int taskListSize(){
		return UploadTaskList.instance().taskList.size();
	}
	
	private void executeNextTask(){
		if(!taskList.isEmpty()){
			taskList.get(0).execute();
			taskList.remove(0);
			executeNextTask();
		}
	}
}
