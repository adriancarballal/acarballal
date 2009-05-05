package es.udc.acarballal.elmas.ffmpeg.encoder;

import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.ConfigurationParametersManager;
import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.MissingConfigurationParameterException;
import es.udc.acarballal.elmas.ffmpegEncoder.AbstractCodec;
import es.udc.acarballal.elmas.ffmpegEncoder.Codec;

public abstract class AbstractEncoder {
	
	private AbstractCodec basicCodec;	
	
	public AbstractEncoder(String input, String output, String extension){
		basicCodec = new Codec(input, output, extension);
	}
	
	public abstract AbstractCodec getCodec();
	public AbstractCodec getBasicCodec(){	return basicCodec;	}
	
	public String getEncodingCommand(){
		return getCodec().generateCommand();
	}
	
	protected static String getParameter(String parameterName){
		try {
			return ConfigurationParametersManager.getParameter(parameterName);
		} catch (MissingConfigurationParameterException e) {	return null;	}
	}
	
	protected static int getIntParameter(String parameterName){
		try {
			return new Integer(ConfigurationParametersManager.getParameter(parameterName));
		} catch (MissingConfigurationParameterException e) {	return 0;	}
	}
	
}
