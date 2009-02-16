package es.udc.acarballal.elmas.ffmpeg.encoder;

import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.ConfigurationParametersManager;
import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.MissingConfigurationParameterException;

public abstract class AbstractVideoEncoder implements IVideoEncoder{
	
	protected VIDEO_CODEC videoCodec = VIDEO_CODEC.libx264;
	protected VIDEO_SIZE videoSize = VIDEO_SIZE.vga;
	protected int videoBitRate = 200;
	protected String aspectRatio = "4:3";
	protected int frameRate = 30;
	
	protected AUDIO_CODEC audioCodec = AUDIO_CODEC.libfaac;
	protected int audioBitRate = 64;
	protected int audioFrecuence = 44100;
	protected int audioChannels = 1;
	
	private String inputFilePath;
	private String outputFilePath;
	
	private static final String APPLICATION_PATH_PARAMETERS = "ffmpeg/application/path";
	private static String application_path;
	static {
		try {
			application_path = 
				ConfigurationParametersManager.getParameter(APPLICATION_PATH_PARAMETERS);
		} catch (MissingConfigurationParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	protected String getInputFilePath(){
		return inputFilePath;
	}
	
	protected String getOutputFilePath(){
		return outputFilePath;
	}
	
	protected String getApplicationPath(){
		return application_path;
	}
	
	public AbstractVideoEncoder(String inputFilePath, String outputFilePath){
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
	}
	
	public String generateEncodingCommand(){
		return application_path + " -i " + inputFilePath + 
			generateEncodingOptions() +	" " + outputFilePath;
	}
	
	protected abstract String generateEncodingOptions();

	public void setAspectRatio(String ratio) {
		this.aspectRatio = ratio;		
	}

	public void setAudioBitRate(int bitRate) {
		this.audioBitRate = bitRate;
	}
	
	public void setAudioFrecuence(int frecuence){
		this.audioFrecuence = frecuence;
	}

	public void setAudioCodec(AUDIO_CODEC audioCodec) {
		this.audioCodec = audioCodec;
	}

	public void setVideoBitRate(int bitRate) {
		this.videoBitRate = bitRate;
	}

	public void setVideoCodec(VIDEO_CODEC videoCodec) {
		this.videoCodec = videoCodec;		
	}

	public void setVideoSize(VIDEO_SIZE size) {
		this.videoSize = size;
	}
	
	public void setFrameRate(int frameRate){
		this.frameRate = frameRate;
	}
	
	public void setAudioChannels(int channels){
	}

}
