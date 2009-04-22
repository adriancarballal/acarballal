package es.udc.acarballal.elmas.model.adminservice.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import es.udc.acarballal.elmas.ffmpeg.process.IProcess;

public class LogDelete implements IProcess{
	
	private List<File> logs;
	
	public LogDelete(List<File> logs){
		this.logs=logs;
	}

	public boolean execute() {
		boolean flag = true;
		for (Iterator iterator = logs.iterator(); iterator.hasNext();) {
			File log = (File) iterator.next();
			flag = flag && log.delete();
		}
		return flag;
	}
	
	public void undo(){}

}
