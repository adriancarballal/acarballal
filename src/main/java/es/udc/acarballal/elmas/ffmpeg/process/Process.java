package es.udc.acarballal.elmas.ffmpeg.process;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import es.udc.acarballal.elmas.ffmpeg.encoder.IVideoEncoder;
import es.udc.acarballal.elmas.ffmpeg.process.exceptions.NoCommandAvailableException;
import es.udc.acarballal.elmas.ffmpeg.process.exceptions.ProcessErrorException;

public class Process implements IProcess{
	
	private final String logFile;
	private IVideoEncoder encoder;
	private static final String FILE_FORMAT = "ISO8859-1";
	protected OutputStreamWriter logWriter = null;
	
	public Process(String logFileName, IVideoEncoder encoder){
		this.logFile = logFileName;
		this.encoder = encoder;
		try {
			logWriter = new OutputStreamWriter(new FileOutputStream(logFile), FILE_FORMAT);
		} catch (Exception e) {
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
			return false;
		}
		return true;
	}
	
	private boolean run() throws NoCommandAvailableException, ProcessErrorException{
		
		// TODO QUITAR PRITNLN
		System.out.println(encoder.generateEncodingCommand());
		String[] cmd = encoder.generateEncodingCommand().split(" ");
		if(cmd==null) throw new NoCommandAvailableException();
        try{            
            Runtime rt = Runtime.getRuntime();
            java.lang.Process proc = rt.exec(cmd);
            
            StreamGobbler errorGobbler = new 
                StreamGobbler(proc.getErrorStream(), "ERROR", logWriter);            
            StreamGobbler outputGobbler = new 
                StreamGobbler(proc.getInputStream(), "OUTPUT", logWriter);
                
            errorGobbler.start();
            outputGobbler.start();
                                    
            if (proc.waitFor()==0)
            	logWriter.write("\nDONE> Build succesfull.\n");
            else{
            	logWriter.write("\nPROBLEM> Unsuccesfull.\n");
            	throw new ProcessErrorException("Build Unsuccesfull.");
            }
        } 
        catch (Throwable t){
        	t.printStackTrace();
        	throw new ProcessErrorException("Error during StreamGlobber.");
        }
        return true;

	}
	
	public void undo(){
		try {
			logWriter.close();
		} catch (IOException e) {
			//UNREACHABLE
		}
	}
	
}
