package es.udc.acarballal.plugins.avidemux.codecs;

import es.udc.acarballal.plugins.avidemux.configuration.*;

//Uncomment for testing.
import es.udc.acarballal.plugins.avidemux.exceptions.*;

/**
 * 
 * @author Adrian Carballal Mato
 * @version 0.1 29/10/2008
 * 
 */
public class FlvEncoder extends Encoder{
	
	private static final String GENERAL_OPTIONS_PARAMETERS = "application/flv/options";
	
	static {
		
		try {
			
			general_options = ConfigurationParametersManager.getParameter(GENERAL_OPTIONS_PARAMETERS);
			
		} catch (MissingConfigurationParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * Constructor used to create this object.
	 * @param input_path Path to the input video archive.
	 * @param output_path Path to the input video archive.
	 */
	public FlvEncoder(String input_path, String output_path){
		
		super(input_path, output_path);				
									
	}
	
	
	//Test code. Uncomment for testing. 
    public static void main (String[] args) {
    	
		System.out.println("Encoding...");
    	long start = System.currentTimeMillis();
    	FlvEncoder encFLV = new FlvEncoder("\"C:\\Documents and Settings\\Adrian\\Escritorio\\input.avi\"", 
    			"\"C:\\Documents and Settings\\Adrian\\Escritorio\\output.flv\"");
    	
    	try {
    		
			encFLV.encode();
			
		} catch (EncodingFailureException e) {
			
			System.out.println(e.getMessage());
			
		} catch (EncoderParametersException e) {
			
			System.out.println(e.getMessage());
			
		}
		
    	long end = System.currentTimeMillis();
    	System.out.println("Execution time was "+(end-start)+" ms.");

    }

}
