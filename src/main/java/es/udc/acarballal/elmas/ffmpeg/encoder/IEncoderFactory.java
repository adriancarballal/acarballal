package es.udc.acarballal.elmas.ffmpeg.encoder;

public interface IEncoderFactory {
	
	public static enum ENCODER_TYPE {WEBPORTAL, MOBILEPORTAL, SNAPSHOT};
	
	public IVideoEncoder createEncoder(ENCODER_TYPE type, String inputFilePath, String outputFilePath);

}
