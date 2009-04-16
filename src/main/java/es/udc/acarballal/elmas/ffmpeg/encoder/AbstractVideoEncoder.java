package es.udc.acarballal.elmas.ffmpeg.encoder;

import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.ConfigurationParametersManager;
import es.udc.acarballal.elmas.ffmpeg.encoder.configuration.MissingConfigurationParameterException;

public abstract class AbstractVideoEncoder implements IVideoEncoder{
	
	public enum VIDEO_SIZE 		{sqcif, qcif, cif, qvga, vga, svga};
	public enum VIDEO_CODEC 	{libx264};
	public enum AUDIO_CODEC 	{libfaac};
	
	private VIDEO_CODEC videoCodec = VIDEO_CODEC.libx264;
	private VIDEO_SIZE videoSize = VIDEO_SIZE.vga;
	private int videoBitRate = 200;
	private String aspectRatio = "4:3";
	private int frameRate = 30;
	
	private AUDIO_CODEC audioCodec = AUDIO_CODEC.libfaac;
	private int audioBitRate = 64;
	private int audioFrecuence = 44100;
	private int audioChannels = 1;
	
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
	
	public AbstractVideoEncoder(String inputFilePath, String outputFilePath){
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
	}
	
	public String generateEncodingCommand(){
		return application_path + " -i " + inputFilePath + 
			generateEncodingOptions() +	" " + outputFilePath;
	}
	
	public abstract String generateEncodingOptions();
	
	public VIDEO_CODEC getVideoCodec() {
		return videoCodec;
	}

	public void setVideoCodec(VIDEO_CODEC videoCodec) {
		this.videoCodec = videoCodec;
	}

	public VIDEO_SIZE getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(VIDEO_SIZE videoSize) {
		this.videoSize = videoSize;
	}

	public int getVideoBitRate() {
		return videoBitRate;
	}

	public void setVideoBitRate(int videoBitRate) {
		this.videoBitRate = videoBitRate;
	}

	public String getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(String aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public int getFrameRate() {
		return frameRate;
	}

	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}

	public AUDIO_CODEC getAudioCodec() {
		return audioCodec;
	}

	public void setAudioCodec(AUDIO_CODEC audioCodec) {
		this.audioCodec = audioCodec;
	}

	public int getAudioBitRate() {
		return audioBitRate;
	}

	public void setAudioBitRate(int audioBitRate) {
		this.audioBitRate = audioBitRate;
	}

	public int getAudioFrecuence() {
		return audioFrecuence;
	}

	public void setAudioFrecuence(int audioFrecuence) {
		this.audioFrecuence = audioFrecuence;
	}

	public int getAudioChannels() {
		return audioChannels;
	}

	public void setAudioChannels(int audioChannels) {
		this.audioChannels = audioChannels;
	}	

}
