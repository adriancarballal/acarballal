package es.udc.acarballal.elmas.model.adminservice.thread;

import java.util.ArrayList;
import java.util.List;

import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.ConfigurationParametersManager;
import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.MissingConfigurationParameterException;

public class ThreadCentral {
	
	private final static String MAX_PARALLELS_PROCESSES_PARAMATER = "max/parallels/processes";
	
	private static ThreadCentral instance;
	private List<ThreadList> queue = new ArrayList<ThreadList>();
	private int index = 0;
	private static int maxParallelThreads;
	
	static{
		try {
			maxParallelThreads = new Integer(
				ConfigurationParametersManager.getParameter(MAX_PARALLELS_PROCESSES_PARAMATER));
		} catch (MissingConfigurationParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private void next(){
		index ++;
		if(index==queue.size()) index = 0;
	}
	
	private ThreadCentral(){
		for(int i=0; i<maxParallelThreads; i++){
			queue.add(new ThreadList());
		}
	}
	
	public static ThreadCentral instance(){
		if(instance == null) instance = new ThreadCentral();
		return instance;
	}
	
	public void addThread(TaskThread ct){
		ThreadList actual = queue.get(index);
		actual.addThread(ct);
		ct.setParentList(actual);
		next();	
	}	

}
