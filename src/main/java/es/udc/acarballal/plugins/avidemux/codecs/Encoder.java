package es.udc.acarballal.plugins.avidemux.codecs;

import java.io.IOException;
import es.udc.acarballal.plugins.avidemux.configuration.*;
import es.udc.acarballal.plugins.avidemux.exceptions.*;

/**
 * 
 * @author Adrian Carballal Mato
 * @version 0.1 29/10/2008
 * 
 */
public abstract class Encoder {

	private String application_path;
	protected static String general_options;
	private String input_path, output_path;
	private static final String APPLICATION_PATH_PARAMETER ="application/path";
	
	/**
	 * Constructor used to create this object.
	 * @param input_path Path to the input video archive.
	 * @param output_path Path to the input video archive.
	 */
	public Encoder(String input_path, String output_path){
		
		try {
			
			this.application_path = 
				ConfigurationParametersManager.getParameter(APPLICATION_PATH_PARAMETER);
			
		} catch (MissingConfigurationParameterException e) {
			
			e.printStackTrace();
			
		};
		
		this.input_path = input_path;
		this.output_path = output_path;

	}
	
	/**
	 * Generate an Array of arguments to use in the command line client.
	 * @return All the arguments to use in command line with Avidemux.
	 */
	private String[] command(){
		
		String cmd = this.application_path + " " + this.input_path + " -o " + 	
			this.output_path + " " + this.getGeneral_options();
		
		return cmd.split(" ");

	}
	
	/**
	 * Executes the encoding action.
	 * @throws EncodingFailureException when encoding didn't finish correctly.
	 * @throws EncoderParametersException when parameters are incorrect.
	 */
	public void encode() throws EncodingFailureException, EncoderParametersException{
		
		try{
			
			Runtime.getRuntime().exec(command());
			
		}catch (IOException e) {
			
			e.printStackTrace();
			throw new EncoderParametersException();
			
		}
		catch (Exception e){
				
			e.printStackTrace();
			throw new EncodingFailureException();
			
		}
		
	}
	
	public String getGeneral_options() {
		return general_options;
	}
	
}
