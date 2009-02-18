package es.udc.acarballal.elmas.ffmpeg.encoder;

public class SnapshotEncoder extends AbstractVideoEncoder{
	
	private int second;
	private static final String extension = ".jpg";
	
	public SnapshotEncoder(String videoSource, String snapshot, int second) {
		super(videoSource, snapshot + extension);
		this.second = second;
	}

	@Override
	protected String generateEncodingOptions() {
		return " -t " + second + " -ss " + second + " -vframes 1";
	}
	
}
