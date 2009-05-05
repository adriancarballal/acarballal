package es.udc.acarballal.elmas.model.adminservice.thread;

import java.util.ArrayList;
import java.util.List;

public class ThreadCentral {
	
	private List<ThreadList> queue = new ArrayList<ThreadList>();
	private int index = 0;
	
	private void next(){
		index ++;
		if(index==queue.size()) index = 0;
	}
	
	public ThreadCentral(int maxThreads){
		for(int i=0; i<maxThreads; i++){
			queue.add(new ThreadList("Thread" + i));
		}
	}
	
	public void addThread(CurrentThread ct){
		ThreadList actual = queue.get(index);
		actual.addThread(ct);
		ct.setParentList(actual);
		next();	
	}
	
	
	

}
