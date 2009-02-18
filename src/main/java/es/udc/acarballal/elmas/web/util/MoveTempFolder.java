package es.udc.acarballal.elmas.web.util;

import java.io.File;

import es.udc.acarballal.elmas.ffmpeg.process.IProcess;

public class MoveTempFolder implements IProcess{

	private String folder;
	private String destination;
	
	public MoveTempFolder(String folder, String destination){
		this.folder = folder;
		this.destination = destination;
	}
	
	public boolean execute() {
		return (new File(folder)).renameTo(new File(destination));
	}

}
