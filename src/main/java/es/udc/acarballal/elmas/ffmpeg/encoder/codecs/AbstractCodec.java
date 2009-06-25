package es.udc.acarballal.elmas.ffmpeg.encoder.codecs;

import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.ConfigurationParametersManager;
import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.MissingConfigurationParameterException;

public abstract class AbstractCodec {
	
	private static final String APPLICATION_PATH_PARAMETERS = "ffmpeg/application/path";
	private static String application_path;
	
	
	static {
		try {
			application_path = 
				ConfigurationParametersManager.getParameter(APPLICATION_PATH_PARAMETERS);
		} catch (MissingConfigurationParameterException e) {
			e.printStackTrace();
		}		
	}
	
	public String generateCommand() {
		return application_path + " -i " + getInput() + generateVideoOptions() + 
		generateAudioOptions() + generateImageOptions() + " " + getOutput();
	}
	
	public abstract String generateVideoOptions();
	public abstract String generateAudioOptions();
	public abstract String generateImageOptions();
	public abstract String getOutput();
	public abstract String getInput();
}
