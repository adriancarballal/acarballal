package es.udc.acarballal.elmas.model.encoderservice;

import org.springframework.transaction.annotation.Transactional;

import es.udc.acarballal.elmas.model.encoderservice.util.EncodeProcess;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.acarballal.elmas.model.videoservice.VideoService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Transactional
public class EncoderServiceImpl implements EncoderService{
	
	private static final Long AdminMessage = new Long(1);

	private VideoService videoService;
	private UserService userService;
	
	public void setVideoService(VideoService videoService){
		this.videoService = videoService;
	}
	
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	public void encodeVideo(String fileAbsolutePath, Long userProfileId,
			String title, String comment) {
		
		EncodeProcess hilo = new EncodeProcess(fileAbsolutePath, userProfileId,
				title, comment, videoService, this);
		hilo.start();
		
	}	
	
	public void sendErrorMessage(Long to, String message) {
		try {
			userService.sendMessage(AdminMessage, to, message);
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			//NOT BUG. NEVER REACHED

		}
	}

	public void sendConfirmationMessage(Long to, String message) {
		try {
			userService.sendMessage(AdminMessage, to, message);
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			//NOT BUG. NEVER REACHED

		}
	}

}
