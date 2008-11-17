package es.udc.acarballal.plugins.avidemux.exceptions;

/**
 * 
 * @author Adrian Carballal Mato
 * @version 0.1 29/10/2008
 * 
 */
public class EncodingFailureException extends Exception {

	/**
	 * Constructor used to create this object.
	 * Indicates an error during an encoding action from the client.
	 */
	public EncodingFailureException() {
		super("Internal error during encoding.");
	}

}
