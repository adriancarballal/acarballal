package es.udc.acarballal.elmas.ffmpeg.encoder;

public class RealPlayerEncoder extends AbstractVideoEncoder{
	
	public RealPlayerEncoder(String inputFilePath, String outputFilePath) {
		super(inputFilePath, outputFilePath + ".3gp");
		this.setVideoSize(VIDEO_SIZE.sqcif);
		this.setFrameRate(25);
		this.setVideoBitRate(300);
		this.setAudioFrecuence(44100);
	}
	
	public String generateEncodingOptions() {
		//LOW QUALITY
		return " -t 90 -s " + getVideoSize() +  
		" -r " + getFrameRate() + " -acodec " + getAudioCodec() + " -ab " + getAudioBitRate() + " -ac " + 
		getAudioChannels() + " -ar " + getAudioFrecuence();
	}
	
}
