	package es.udc.acarballal.plugins.avidemux.exceptions;

/**
 * 
 * @author Adrian Carballal Mato
 * @version 0.1 29/10/2008
 * 
 */
public abstract class ClientException extends Exception{

	private static String ERROR_MESSAGE;
	
	/**
	 * Constructor used to create this object.
	 * @param error_message String referencing the error exception.
	 */
	public ClientException(String error_message){
		ERROR_MESSAGE = error_message;
	}
	
	/**
	 * Returns the error message this object was created. The error message is set within the constructor.
	 * @return the encapsulated error message .
	 */
	public String getMessage(){
		 return ERROR_MESSAGE;
	}
}
