package es.udc.acarballal.mencoder;

import es.udc.acarballal.mencoder.configuration.*;

public class MiniFlvEncoder extends Encoder{

	private static final String GENERAL_OPTIONS_PARAMETERS = "application/miniflv/options";
	
	static {
		try {
			general_options = 
				ConfigurationParametersManager.getParameter(GENERAL_OPTIONS_PARAMETERS);
		} catch (MissingConfigurationParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public MiniFlvEncoder(String input_file, String output_file, String logfile) {
		super(input_file, output_file, logfile);
		
	}

}