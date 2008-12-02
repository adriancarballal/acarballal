package es.udc.acarballal.elmas.model.exceptions;

@SuppressWarnings("serial")
public class InvalidOperationException extends Exception{
	
private String reason;
    
    public InvalidOperationException(String reason) {
        super("Invalid Operation exception => reason = " + reason);
        this.reason = reason;
    }
    
    public String getLoginName() {
        return reason;
    }

}
