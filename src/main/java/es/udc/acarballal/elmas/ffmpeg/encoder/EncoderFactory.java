package es.udc.acarballal.elmas.ffmpeg.encoder;

public class EncoderFactory extends AbstractEncoderFactory{

	public AbstractEncoder createFlvEncoder(String input, String output){
		return new FlvEncoder(input, output);
	}
	public AbstractEncoder createRealPlayerEncoder(String input, String output){
		return new RPlayerEncoder(input, output);
	}
	public AbstractEncoder createSnapshotEncoder(String input, String output){
		return new SnapShotEncoder(input, output);
	}
}
