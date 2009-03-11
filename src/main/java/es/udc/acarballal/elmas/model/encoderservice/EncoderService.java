package es.udc.acarballal.elmas.model.encoderservice;

public interface EncoderService {
	
	public void sendConfirmationMessage(Long to, String message);
	
	public void sendErrorMessage(Long to, String message);
	
	public void encodeVideo(String fileAbsolutePath, 
    		Long userProfileId,String title,String comment);
	
}
