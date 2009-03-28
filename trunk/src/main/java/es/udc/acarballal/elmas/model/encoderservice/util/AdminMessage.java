package es.udc.acarballal.elmas.model.encoderservice.util;

import es.udc.acarballal.elmas.ffmpeg.process.IProcess;
import es.udc.acarballal.elmas.model.encoderservice.EncoderService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class AdminMessage implements IProcess{
	
	private EncoderService encoderService;
	private Long to;
	private String text;
	
	public AdminMessage(EncoderService encoderService, Long to, String text){
		this.encoderService = encoderService;
		this.to = to;
		this.text = text;
	}
	
	public boolean execute() {
		try {
			encoderService.sendConfirmationMessage(to, text);
		} catch (InstanceNotFoundException e) {
			return false;
		}
		return true;
	}
	
	public void undo(){}

}
