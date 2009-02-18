package es.udc.acarballal.elmas.ffmpeg.encoder;

public class FlvEncoder extends AbstractVideoEncoder{
	
	private static final String extension = ".flv";

	public FlvEncoder(String inputFilePath, String outputFilePath) {
		super(inputFilePath, outputFilePath + extension);
		this.setVideoSize(VIDEO_SIZE.cif);
	}

	@Override
	protected String generateEncodingOptions() {
		return " -f flv -t 90 -s " + videoSize + " -ar " + audioFrecuence + " ";
	}
	
}
