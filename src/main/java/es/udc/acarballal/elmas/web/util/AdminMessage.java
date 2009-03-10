package es.udc.acarballal.elmas.web.util;

import es.udc.acarballal.elmas.ffmpeg.process.IProcess;
import es.udc.acarballal.elmas.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class AdminMessage implements IProcess{
	
	private UserService userService;
	private Long from, to;
	private String text;
	
	public AdminMessage(UserService userService, Long to, String text){
		this.userService = userService;
		this.from = new Long(1);
		this.to = to;
		this.text = text;
	}
	
	public boolean execute() {
		try {
			userService.sendMessage(from, to, text);
		} catch (InstanceNotFoundException e) {
			return false;
		}
		return true;
	}


}
