package es.udc.acarballal.elmas.ffmpeg.process.util;

import java.io.File;

public class DirectoryGenerator {
	
	private static final int stringLength = 8;
	
	/**
	 * @param path to directory ended with /
	 * @return Create Directory File
	 */
	public static File create(String path){
		
		File dir = new File(path + NameGenerator.generateString(stringLength));
		boolean created = dir.mkdir();
		while (!created){
			dir = new File(path + NameGenerator.generateString(stringLength));
			created = dir.mkdir();
		}
		return dir;
	}
	
	public static int getStringLength(){
		return stringLength;
	}

}
