package es.udc.acarballal.elmas.ffmpeg.encoder;

public class FlvEncoder extends AbstractVideoEncoder{
	
	public FlvEncoder(String inputFilePath, String outputFilePath) {
		super(inputFilePath, outputFilePath + ".flv");
		this.setVideoSize(VIDEO_SIZE.cif);
	}

	@Override
	public String generateEncodingOptions() {
		return " -f flv -t 90 -s " + getVideoSize() + " -ar " + getAudioFrecuence() + " ";
	}
	
}
