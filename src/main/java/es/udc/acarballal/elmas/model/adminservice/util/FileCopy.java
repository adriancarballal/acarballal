package es.udc.acarballal.elmas.model.adminservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import es.udc.acarballal.elmas.ffmpeg.process.IProcess;

public class FileCopy implements IProcess{
	
	private String sourceFile;
	private String destinationFile;
	
	public FileCopy(String sourceFile, String destinationFile){
		this.sourceFile = sourceFile;
		this.destinationFile = destinationFile;
	}

	public boolean execute() {
		File inputFile = new File(sourceFile);
	    File outputFile = new File(destinationFile);

	    InputStream in;
		try {
			in = new FileInputStream(inputFile);
	        OutputStream out = new FileOutputStream(outputFile);
	        
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) out.write(buf, 0, len);

	        in.close();
	        out.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	    return true;
	}
	
	public void undo(){}

}
