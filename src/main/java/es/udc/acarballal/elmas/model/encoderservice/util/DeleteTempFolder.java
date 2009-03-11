package es.udc.acarballal.elmas.model.encoderservice.util;

import java.io.File;

import es.udc.acarballal.elmas.ffmpeg.process.IProcess;

public class DeleteTempFolder implements IProcess{

	private String folder;
		
	public DeleteTempFolder(String folder){
		this.folder = folder;
	}
	
	public boolean execute() {
		File[] files = (new File(folder)).listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
		return (new File(folder)).delete();
	}
	
	public void undo(){}
}
