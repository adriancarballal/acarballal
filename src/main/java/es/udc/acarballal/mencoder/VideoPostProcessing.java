package es.udc.acarballal.mencoder;

import java.io.File;

import es.udc.acarballal.mencoder.util.DirectoryGenerator;
import es.udc.acarballal.mencoder.util.process.Process;

public class VideoPostProcessing {
	
	static String PATH_TO_ORIGINAL_VIDEO;
	static String PATH_TO_TEMP_DIRECTORY = "C:\\carpeta_apache\\temporal\\";
	static String ORIGINAL_FILE = "C:\\input3.mp4";
	
	final static String fileID = "\\101";	

	public static void main(String[] args) {
		
		String videoName; String encodeLog; Process encoder;
		boolean cont = true;
		
		File temp_dir = DirectoryGenerator.create(PATH_TO_TEMP_DIRECTORY);
		
		videoName = temp_dir.getAbsolutePath() + fileID + ".flv";
		encodeLog = temp_dir.getAbsolutePath() + "\\flvEncoding.log";
		encoder = new FlvEncoder(ORIGINAL_FILE, videoName, encodeLog); 
		cont = encoder.execute();
		if(cont){
			videoName = temp_dir.getAbsolutePath() + fileID + "_mini.flv";
			encodeLog = temp_dir.getAbsolutePath() + "\\miniFlvEncoding.log";
			encoder = new MiniFlvEncoder(ORIGINAL_FILE, videoName, encodeLog); 
			cont = encoder.execute();
		}
		
		
	}
}
