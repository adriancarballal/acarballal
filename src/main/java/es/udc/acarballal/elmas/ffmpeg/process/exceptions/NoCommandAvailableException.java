package es.udc.acarballal.elmas.ffmpeg.process.exceptions;

@SuppressWarnings("serial")
public class NoCommandAvailableException extends Exception{
		
	private final static String error = "None available command.";
    
    public NoCommandAvailableException() {
        super("Process error exception => " + error);
    }
    
    public String getError() {
        return error;
    }
}