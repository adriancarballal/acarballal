package es.udc.acarballal.elmas.model.adminservice.util;

import es.udc.acarballal.elmas.ffmpeg.process.IProcess;
import es.udc.acarballal.elmas.model.adminservice.AdminService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class AdminMessage implements IProcess{
	
	private AdminService adminService;
	private Long to;
	private String text;
	
	public AdminMessage(AdminService adminService, Long to, String text){
		this.adminService = adminService;
		this.to = to;
		this.text = text;
	}
	
	public boolean execute() {
		try {
			adminService.sendConfirmationMessage(to, text);
		} catch (InstanceNotFoundException e) {
			return false;
		}
		return true;
	}
	
	public void undo(){}

}
