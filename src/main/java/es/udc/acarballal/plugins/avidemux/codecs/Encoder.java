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

	private String application_path, general_options;
	private String input_path, output_path;
	private String audio_options, video_options;
	private static final String APPLICATION_PATH_PARAMETER ="application/path";
	private static final String GENERAL_OPTIONS_PARAMETERS = "application/general/options";
	
	/**
	 * Constructor used to create this object.
	 * @param input_path Path to the input video archive.
	 * @param output_path Path to the input video archive.
	 * @param audio_options Audio options used by the Avidemux client.
	 * @param video_options Video options used by the Avidemux client.
	 */
	public Encoder(String input_path, String output_path, String audio_options, String video_options){
		
		try {
			
			this.application_path = 
				ConfigurationParametersManager.getParameter(APPLICATION_PATH_PARAMETER);
			this.general_options = 
				ConfigurationParametersManager.getParameter((GENERAL_OPTIONS_PARAMETERS));
			
		} catch (MissingConfigurationParameterException e) {
			
			e.printStackTrace();
			
		};
		
		this.input_path = input_path;
		this.output_path = output_path;
		this.audio_options = audio_options;
		this.video_options = video_options;

	}
	
	/**
	 * Generate an Array of arguments to use in the command line client.
	 * @return All the arguments to use in command line with Avidemux.
	 */
	private String[] command(){
		
		String cmd = this.getApplication_path() + " " + this.getGeneral_options() +
			" " + this.getInput_path() + " " + this.getAudio_options() + " " +
			this.getVideo_options() + " " + this.getOutput_path() + " --quit";

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
			
			//e.printStackTrace();
			throw new EncoderParametersException();
			
		}
		catch (Exception e){
				
			//e.printStackTrace();
			throw new EncodingFailureException();
			
		}
		
	}
	
	/**
	 * Returns the application path this object was created. 
	 * @return the application path to avidemux client.
	 */
	public String getApplication_path() {
		return application_path;
	}

	/**
	 * Returns the audio options this object was created. 
	 * The audio options are set within the constructor.
	 * @return audio options used by the Avidemux client.
	 */
	public String getAudio_options() {
		return audio_options;
	}

	/**
	 * Returns the video options this object was created. 
	 * The video options is set within the constructor.
	 * @return video options used by the Avidemux client.
	 */
	public String getVideo_options() {
		return video_options;
	}

	/**
	 * Returns the output path this object was created. 
	 * The output path is set within the constructor.
	 * @return path to the output video archive.
	 */
	public String getOutput_path() {
		return "--save " + output_path;
	}

	/**
	 * Returns the general options this object was created. 
	 * @return the general options used by the client.
	 */
	public String getGeneral_options() {
		return general_options;
	}

	/**
	 * Returns the input path this object was created. 
	 * The input path is set within the constructor.
	 * @return path to the input video archive.
	 */
	public String getInput_path() {
		return "--load " + input_path;
	}

}
