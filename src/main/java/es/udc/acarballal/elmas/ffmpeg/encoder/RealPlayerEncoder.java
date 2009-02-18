package es.udc.acarballal.elmas.ffmpeg.encoder;

public class RealPlayerEncoder extends AbstractVideoEncoder{
	
	private static final String extension = ".3gp";

	public RealPlayerEncoder(String inputFilePath, String outputFilePath) {
		super(inputFilePath, outputFilePath + extension);
		this.setVideoSize(VIDEO_SIZE.sqcif);
		this.setFrameRate(25);
		this.setVideoBitRate(300);
		this.setAudioFrecuence(44100);
		
	}
	
	protected String generateEncodingOptions() {
		//LOW QUALITY
		/*return " -t 90 -s " + videoSize + " -b " + videoBitRate + 
		" -r " + frameRate + " -acodec " + audioCodec + " -ab " + audioBitRate + " -ac " + 
		audioChannels + " -ar " + audioFrecuence;*/
		return " -t 90 -s " + videoSize +  
		" -r " + frameRate + " -acodec " + audioCodec + " -ab " + audioBitRate + " -ac " + 
		audioChannels + " -ar " + audioFrecuence;
	}
	
}
