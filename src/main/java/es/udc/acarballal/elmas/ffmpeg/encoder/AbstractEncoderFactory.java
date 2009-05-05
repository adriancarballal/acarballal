package es.udc.acarballal.elmas.ffmpeg.encoder;

public abstract class AbstractEncoderFactory {

	public abstract AbstractEncoder createFlvEncoder(String input, String output);
	public abstract AbstractEncoder createRealPlayerEncoder(String input, String output);
	public abstract AbstractEncoder createSnapshotEncoder(String input, String output);
}
