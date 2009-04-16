package es.udc.acarballal.elmas.ffmpeg.encoder;

public class SnapshotEncoder extends AbstractVideoEncoder{
	
	private int second;
	
	public SnapshotEncoder(String videoSource, String snapshot, int second) {
		super(videoSource, snapshot + ".jpg");
		this.second = second;
	}

	@Override
	public String generateEncodingOptions() {
		return " -t " + second + " -ss " + second + " -vframes 1";
	}
	
}
