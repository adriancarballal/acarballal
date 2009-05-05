package es.udc.acarballal.elmas.model.adminservice.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.sun.jmx.remote.internal.ArrayQueue;

public class ThreadList extends Thread{
	
	private List<Thread> list = new ArrayList<Thread>();
	private String id;
	
	private boolean inUse = false;

	public ThreadList(String id){
		this.id = id;
	}
	
	public synchronized void addThread(CurrentThread t){
		
		list.add(t);
		System.out.println("lista " + id + " - añadido thread: " + t.getIdNumber());
		System.out.println("TOTAL: " + list.size());
		if(list.size()!=0) {			
			run(); 
		}
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
