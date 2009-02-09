package es.udc.acarballal.mencoder.util.process;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public abstract class Process {
	
	private final String logFile;
	private static final String FILE_FORMAT = "ISO8859-1";
	protected OutputStreamWriter logWriter = null;
	
	public Process(String path, String logFileName){
		this.logFile = logFileName;
		try {
			logWriter = new OutputStreamWriter(new FileOutputStream(path + logFile), FILE_FORMAT);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean execute(){
		
		if(logWriter==null) return false;
		try {
			logWriter.write("INIT> STARTING PROCESS.\n\n");
			run();
			logWriter.flush();
			logWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public abstract boolean run();
	
}
