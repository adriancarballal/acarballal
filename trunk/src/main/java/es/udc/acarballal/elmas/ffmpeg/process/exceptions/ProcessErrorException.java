package es.udc.acarballal.elmas.ffmpeg.process.exceptions;

@SuppressWarnings("serial")
public class ProcessErrorException extends Exception{
		
	private String error;
    
    public ProcessErrorException(String error) {
        super("Process error exception => " + error);
        this.error = error;
    }
    
    public String getError() {
        return error;
    }
}

