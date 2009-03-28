package es.udc.acarballal.elmas.model.encoderservice;

import es.udc.acarballal.elmas.model.exceptions.IncorrectPasswordException;
import es.udc.acarballal.elmas.model.exceptions.InsufficientPrivilegesException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface EncoderService {
	
	public void sendConfirmationMessage(Long to, String message) 
			throws InstanceNotFoundException;
	
	public void sendErrorMessage(Long to, String message) 
			throws InstanceNotFoundException;
	
	public void encodeVideo(String fileAbsolutePath, 
    	Long userProfileId,String title,String comment) 
			throws InstanceNotFoundException, IncorrectPasswordException, 
			InsufficientPrivilegesException;
	
}
