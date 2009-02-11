package es.udc.acarballal.mencoder;

import es.udc.acarballal.mencoder.configuration.*;
import es.udc.acarballal.mencoder.util.process.NoCommandAvailableException;
import es.udc.acarballal.mencoder.util.process.Process;
import es.udc.acarballal.mencoder.util.process.ProcessErrorException;
import es.udc.acarballal.mencoder.util.process.ProcessExecuter;

public abstract class Encoder extends Process{

	private static final String APPLICATION_PATH_PARAMETER ="application/path";
	protected static String general_options;
	private String application_path;
	private String input_file;
	private String output_file;
	
	public Encoder(String input_file, String output_file, String logFile){
		super(logFile);		
		try {
			this.application_path = 
				ConfigurationParametersManager.getParameter(APPLICATION_PATH_PARAMETER);
		} catch (MissingConfigurationParameterException e) {
			// DO NOT BE REACHEABLE
			e.printStackTrace();
		};
		this.input_file = input_file;
		this.output_file = output_file;
	}
	
	private String command(){
		return this.application_path + " " + this.input_file + " -o " + 	
			this.output_file + " " + this.getGeneral_options();
	}
	
	protected boolean run(){
		System.out.println(command());
		try {
			ProcessExecuter.execute(command().split(" "), logWriter);
		} catch (ProcessErrorException e) {
			e.printStackTrace();
			return false;
		} catch (NoCommandAvailableException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String getGeneral_options() {
		return general_options;
	}
	
}
