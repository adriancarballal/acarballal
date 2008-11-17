package es.udc.acarballal.plugins.avidemux.exceptions;

/**
 * 
 * @author Adrian Carballal Mato
 * @version 0.1 29/10/2008
 * 
 */
public class EncoderParametersException extends ClientException{
	
	/**
	 * Constructor used to create this object.
	 * Indicates an invalid number or type of parameters for the client.
	 */
	public EncoderParametersException(){
		super("Invalid Parameters in Avidemux Client.");
	}
	
}
