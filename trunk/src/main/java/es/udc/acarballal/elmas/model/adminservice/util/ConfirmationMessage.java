package es.udc.acarballal.elmas.model.adminservice.util;

import es.udc.acarballal.elmas.ffmpeg.process.IProcess;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ConfirmationMessage implements IProcess{
	
	private final static Long ADMIN_PROFILE_ID_MESSAGING = new Long(1);
	
	private UserService userService;
	private Long to;
	private String text;
	
	public ConfirmationMessage(UserService userService, Long to, String text){
		this.userService = userService;
		this.to = to;
		this.text = text;
	}
	
	public boolean execute() {
		try {
			userService.sendMessage(ADMIN_PROFILE_ID_MESSAGING, to, text);
		} catch (InstanceNotFoundException e) {
			return false;
		}
		return true;
	}
	
	public void undo(){}

}
