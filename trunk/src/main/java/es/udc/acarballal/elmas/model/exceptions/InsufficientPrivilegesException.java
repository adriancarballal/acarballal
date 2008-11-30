package es.udc.acarballal.elmas.model.exceptions;

@SuppressWarnings("serial")
public class InsufficientPrivilegesException extends Exception{
	
	private String loginName;
    
    public InsufficientPrivilegesException(String loginName) {
        super("Insufficient privileges exception => loginName = " + loginName);
        this.loginName = loginName;
    }
    
    public String getLoginName() {
        return loginName;
    }

}
