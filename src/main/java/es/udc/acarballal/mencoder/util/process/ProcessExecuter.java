package es.udc.acarballal.mencoder.util.process;

import java.io.OutputStreamWriter;

public class ProcessExecuter {

	public static void execute(String[] cmd, OutputStreamWriter logWriter) 
			throws ProcessErrorException, NoCommandAvailableException{
		
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
    }

}
