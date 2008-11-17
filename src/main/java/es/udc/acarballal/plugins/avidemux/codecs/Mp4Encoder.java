package es.udc.acarballal.plugins.avidemux.codecs;

import es.udc.acarballal.plugins.avidemux.configuration.*;

/**
 * 
 * @author Adrian Carballal Mato
 * @version 0.1 29/10/2008
 * 
 */
public class Mp4Encoder extends Encoder{
	
	private static String audio_options, video_options;
	private static final String AUDIO_OPTIONS_PARAMETERS = "mp4_options/audio";
	private static final String VIDEO_OPTIONS_PARAMETERS = "mp4_options/video";
	
	static {
		
		try {
			
			audio_options = ConfigurationParametersManager.getParameter(AUDIO_OPTIONS_PARAMETERS);
			video_options = ConfigurationParametersManager.getParameter(VIDEO_OPTIONS_PARAMETERS);
			
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
	public Mp4Encoder(String input_path, String output_path){
		
		super(input_path, output_path, audio_options, video_options);				
									
	}
	
	//Test code. Uncomment for testing. 
//    public static void main (String[] args) {
//    	
//		System.out.println("Encoding...");
//    	long start = System.currentTimeMillis();
//    	Mp4Encoder encMP4 = new Mp4Encoder("\"C:\\Documents and Settings\\Adrian\\Escritorio\\input.mp4\"", 
//		"\"C:\\Documents and Settings\\Adrian\\Escritorio\\output.mp4\"");
//    	try {
//    		
//			encMP4.encode();
//			
//		} catch (EncodingFailureException e) {
//			
//			System.out.println(e.getMessage());
//			
//		} catch (EncoderParametersException e) {
//			
//			System.out.println(e.getMessage());
//			
//		}
//		
//    	long end = System.currentTimeMillis();
//    	System.out.println("Execution time was "+(end-start)+" ms.");
//
//    }

	

}
