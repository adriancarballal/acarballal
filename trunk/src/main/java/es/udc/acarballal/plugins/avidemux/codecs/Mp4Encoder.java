package es.udc.acarballal.plugins.avidemux.codecs;

//import es.udc.acarballal.plugins.avidemux.configuration.*;

/**
 * 
 * @author Adrian Carballal Mato
 * @version 0.1 29/10/2008
 * 
 */
public class Mp4Encoder extends Encoder{
	
	
	static {
		
	}
	
	/**
	 * Constructor used to create this object.
	 * @param input_path Path to the input video archive.
	 * @param output_path Path to the input video archive.
	 */
	public Mp4Encoder(String input_path, String output_path){
		
		super(input_path, output_path);				
									
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
