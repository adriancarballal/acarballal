package es.udc.acarballal.elmas.ffmpeg.encoder;

public class EncoderFactory implements IEncoderFactory{

	public IVideoEncoder createEncoder(ENCODER_TYPE type, String inputFilePath,
			String outputFilePath) {
		if(type.equals(ENCODER_TYPE.MOBILEPORTAL)){
			return new RealPlayerEncoder(inputFilePath, outputFilePath);
		}
		if(type.equals(ENCODER_TYPE.WEBPORTAL)){
			return new FlvEncoder(inputFilePath, outputFilePath);
		}
		return null;
	}
	
	/*public static void main(String[] args) {
		EncoderFactory factory = new EncoderFactory();
		IVideoEncoder enc = factory.createEncoder(ENCODER_TYPE.WEBPORTAL, "input.mp4", "output.flv");
		enc.executeCommand();
		
		IVideoEncoder enc2 = factory.createEncoder(ENCODER_TYPE.MOBILEPORTAL, "input.mp4", "output.flv");
		enc2.executeCommand();
	}
*/
}
