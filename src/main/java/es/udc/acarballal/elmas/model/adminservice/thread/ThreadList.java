package es.udc.acarballal.elmas.model.adminservice.thread;

import java.util.ArrayList;
import java.util.List;

public class ThreadList extends Thread{
	
	private List<Thread> list = new ArrayList<Thread>();
	private boolean inUse = false;

	public synchronized void addThread(TaskThread t){
		list.add(t);
		if(list.size()!=0) run();
	}
	
	public boolean isEmpty(){	return list.isEmpty();	}
	
	public void setOnUse(){		this.inUse = true;		}
	
	public void setOffUse(){	this.inUse = false;		}
	
	public void run(){
		if(!list.isEmpty() && !inUse){
			setOnUse();
			list.remove(0).start();
		}
	}
}
