package es.udc.acarballal.mencoder;

import es.udc.acarballal.mencoder.configuration.*;

public class FlvEncoder extends Encoder{

	private static final String GENERAL_OPTIONS_PARAMETERS = "application/flv/options";
	private static final String LOG_FILE_NAME = "\\flv_encoding.log";
	
	static {
		try {
			general_options = ConfigurationParametersManager.getParameter(GENERAL_OPTIONS_PARAMETERS);
			logFile = LOG_FILE_NAME;
		} catch (MissingConfigurationParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public FlvEncoder(String input_path, String input_file,String output_path, String output_file) {
		super(input_path, input_file, output_path, output_file);
	}

}
