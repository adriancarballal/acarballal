package es.udc.acarballal.elmas.model.encoderservice.util;

import es.udc.acarballal.elmas.ffmpeg.process.IProcess;
import es.udc.acarballal.elmas.model.encoderservice.EncoderService;

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
		encoderService.sendConfirmationMessage(to, text);
		return true;
	}
	
	public void undo(){}

}
