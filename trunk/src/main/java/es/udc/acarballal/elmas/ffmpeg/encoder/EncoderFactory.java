package es.udc.acarballal.elmas.ffmpeg.encoder;

public class EncoderFactory implements IEncoderFactory{
	
	private static final int snapshotTimeSecond = 10;

	public IVideoEncoder createEncoder(ENCODER_TYPE type, String inputFilePath,
			String outputFilePath) {
		if(type.equals(ENCODER_TYPE.MOBILEPORTAL)){
			return new RealPlayerEncoder(inputFilePath, outputFilePath);
		}
		if(type.equals(ENCODER_TYPE.WEBPORTAL)){
			return new FlvEncoder(inputFilePath, outputFilePath);
		}
		if(type.equals(ENCODER_TYPE.SNAPSHOT)){
			return new SnapshotEncoder(inputFilePath, outputFilePath, snapshotTimeSecond);
		}
		return null;
	}
	
}
